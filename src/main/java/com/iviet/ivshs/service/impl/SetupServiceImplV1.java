package com.iviet.ivshs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iviet.ivshs.constant.UrlConstant;
import com.iviet.ivshs.dao.ClientDaoV1;
import com.iviet.ivshs.dao.SetupDaoV1;
import com.iviet.ivshs.dto.SetupRequestV1;
import com.iviet.ivshs.entities.ClientV1;
import com.iviet.ivshs.entities.RoomV1;
import com.iviet.ivshs.enumeration.ClientTypeV1;
import com.iviet.ivshs.exception.domain.BadRequestException;
import com.iviet.ivshs.exception.domain.InternalServerErrorException;
import com.iviet.ivshs.exception.domain.NotFoundException;
import com.iviet.ivshs.service.SetupServiceV1;
import com.iviet.ivshs.util.HttpClientUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SetupServiceImplV1 implements SetupServiceV1 {

    @Autowired
    private ClientDaoV1 clientDao;
    
    @Autowired
    private SetupDaoV1 setupDaoV1;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setup(Long clientId) {
        log.info("[SETUP_SERVICE_START_FETCHING] clientId={}", clientId);

        ClientV1 client = clientDao.findById(clientId)
            .filter(c -> c.getClientType() == ClientTypeV1.HARDWARE_GATEWAY)
            .orElseThrow(() -> {
                log.error("[SETUP_ERR_001] Client not found: id={}", clientId);
                return new NotFoundException("Client not found: id=" + clientId);
            });

        SetupRequestV1 req;
        try {
            var res = HttpClientUtil.get(UrlConstant.getSetupUrlV1(client.getIpAddress()));
            req = HttpClientUtil.fromJson(res.getBody(), SetupRequestV1.class);
        } catch (Exception e) {
            log.error("[SETUP_ERR_002] Failed to fetch setup from clientId={}: {}", clientId, e.getMessage());
            throw new InternalServerErrorException("Failed to fetch setup data from client ID: " + clientId, e);
        }

        log.info("[SETUP_SERVICE_START_PERSISTANCE] totalDevices={}, roomCode={}", req.getDevices() != null ? req.getDevices().size() : 0, req.getRoomCode());

        try {
            if (req == null || req.getRoomCode() == null || req.getRoomCode().isBlank()) {
                log.error("[SETUP_ERR_003] Room code is required");
                throw new BadRequestException("Room code is required");
            }

            if (req.getDevices() == null || req.getDevices().isEmpty()) {
                log.warn("[SETUP_WARN] No devices to setup for room: {}", req.getRoomCode());
                throw new BadRequestException("No devices to setup");
            }

            RoomV1 room = setupDaoV1.findRoomByCode(req.getRoomCode());
            log.debug("[SETUP_ROOM_FOUND] roomId={}, code={}", room.getId(), room.getCode());

            int processedDevices = setupDaoV1.persistDeviceSetup(
                req.getDevices(), 
                client.getId(),
                room.getId()
            );

            log.info("[SETUP_SERVICE_SUCCESS] clientId={}, roomCode={}, createdDevices={}", 
                client.getId(), req.getRoomCode(), processedDevices);

        } catch (BadRequestException e) {
            log.error("[SETUP:VALIDATION_ERR] {}", e.getMessage());
            throw e;
        } catch (NotFoundException e) {
            log.error("[SETUP:NOT_FOUND_ERR] {}", e.getMessage());
            throw e;
        } catch (jakarta.persistence.PersistenceException e) {
            log.error("[SETUP:DB_ERR] DB constraint violation: {}", e.getMessage(), e);
            throw new InternalServerErrorException(
                String.format("[SETUP_FAILED] Database constraint violation: %s", e.getMessage()), e);
        } catch (Exception e) {
            log.error("[SETUP:UNEXPECTED_ERR] Unexpected failure: {}", e.getMessage(), e);
            throw new InternalServerErrorException(
                String.format("[SETUP_FAILED] Unexpected error: %s", e.getMessage()), e);
        }
    }
}
