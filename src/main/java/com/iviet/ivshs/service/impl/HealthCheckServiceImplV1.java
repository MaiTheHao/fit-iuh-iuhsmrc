package com.iviet.ivshs.service.impl;

import com.iviet.ivshs.constant.UrlConstant;
import com.iviet.ivshs.dao.ClientDaoV1;
import com.iviet.ivshs.dao.RoomDaoV1;
import com.iviet.ivshs.dto.HealthCheckResponseDtoV1;
import com.iviet.ivshs.dto.HealthCheckResponseDtoV1.DeviceDto;
import com.iviet.ivshs.exception.domain.BadRequestException;
import com.iviet.ivshs.exception.domain.InternalServerErrorException;
import com.iviet.ivshs.service.HealthCheckServiceV1;
import com.iviet.ivshs.util.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckServiceImplV1 implements HealthCheckServiceV1 {

    private final ClientDaoV1 clientDao;
    private final RoomDaoV1 roomDao;

    @Override
    public HealthCheckResponseDtoV1 checkByClient(Long clientId) {
        String ipAddress = clientDao.findGatewayById(clientId)
                .orElseThrow(() -> new BadRequestException("Client not found ID: " + clientId))
                .getIpAddress();
        return checkByClient(ipAddress);
    }

    @Override
    public HealthCheckResponseDtoV1 checkByClient(String ipAddress) {
        String url = UrlConstant.getHealthUrlV1(ipAddress);
        log.debug("[HealthCheck] Pinging IP: {}", ipAddress);

        try {
            var response = HttpClientUtil.get(url);

            if (!response.isSuccess()) {
                log.warn("[HealthCheck] Failed IP [{}] with status {}", ipAddress, response.getStatusCode());
                throw new InternalServerErrorException("Health check failed with status " + response.getStatusCode());
            }

            var responseDto = HttpClientUtil.fromJson(response.getBody(), HealthCheckResponseDtoV1.class);
            return responseDto;
        } catch (BadRequestException e) {
            log.warn("[HealthCheck] Bad request for IP [{}]: {}", ipAddress, e.getMessage());
            throw e;
        } catch (InternalServerErrorException e) {
            log.error("[HealthCheck] Infrastructure error for IP [{}]: {}", ipAddress, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("[HealthCheck] Response deserialization error for IP [{}]: {}", ipAddress, e.getMessage());
            throw new InternalServerErrorException("Invalid response format from " + ipAddress, e);
        } catch (Exception e) {
            log.error("[HealthCheck] Connection error for IP [{}]: {}", ipAddress, e.getMessage());
            throw new InternalServerErrorException("Connection failed to " + ipAddress, e);
        }
    }

    @Override
    public Map<String, HealthCheckResponseDtoV1> checkByRoom(Long roomId) {
        String roomCode = roomDao.findById(roomId)
                .orElseThrow(() -> new BadRequestException("Room not found ID: " + roomId))
                .getCode();
        return checkByRoom(roomCode);
    }

    @Override
    public Map<String, HealthCheckResponseDtoV1> checkByRoom(String roomCode) {
        List<String> ipAddresses = clientDao.findGatewaysByRoomCode(roomCode).stream()
                .map(client -> client.getIpAddress())
                .toList();

        if (ipAddresses.isEmpty()) {
            throw new BadRequestException("No gateways found for Room: " + roomCode);
        }

        long start = System.currentTimeMillis();
        log.info("[HealthCheck] Batch start room [{}] - {} gateways", roomCode, ipAddresses.size());

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var results = ipAddresses.stream()
                    .map(ip -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return Map.entry(ip, checkByClient(ip));
                        } catch (Exception e) {
                            String domainMessage = mapExceptionToMessage(e);
                            log.warn("[HealthCheck] Gateway [{}] error: {}", ip, domainMessage, e);
                            return Map.entry(ip, HealthCheckResponseDtoV1.builder()
                                    .status(500)
                                    .message(domainMessage)
                                    .timestamp(Instant.now().toString())
                                    .build());
                        }
                    }, executor))
                    .map(CompletableFuture::join)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            log.info("[HealthCheck] Batch finish room [{}] in {}ms", roomCode, System.currentTimeMillis() - start);
            return results;
        }
    }

    private String mapExceptionToMessage(Exception e) {
        if (e == null) {
            return "Unknown error";
        }

        String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        String exceptionType = e.getClass().getSimpleName();

        if (e instanceof java.net.ConnectException || 
            message.contains("connection refused") || 
            message.contains("unreachable")) {
            return "Gateway unreachable";
        }

        if (e instanceof java.net.SocketTimeoutException || 
            e instanceof java.util.concurrent.TimeoutException ||
            message.contains("timeout")) {
            return "Gateway timeout";
        }

        if (e instanceof IllegalArgumentException ||
            message.contains("json") || 
            message.contains("deserialization") ||
            message.contains("invalid response")) {
            return "Invalid response from gateway";
        }

        if (exceptionType.contains("InternalServerError")) {
            return "Gateway service error";
        }

        return "Gateway communication failed";
    }

    @Override
    public int getHealthScoreByClient(Long clientId) {
        try {
            HealthCheckResponseDtoV1 result = checkByClient(clientId);
            return calculateScore(result);
        } catch (Exception e) {
            log.warn("[HealthScore] Client [{}] calculation failed: {}", clientId, e.getMessage());
            return 0;
        }
    }

    @Override
    public int getHealthScoreByRoom(Long roomId) {
        Map<String, HealthCheckResponseDtoV1> roomResults = checkByRoom(roomId);

        if (roomResults.isEmpty()) return 0;

        double averageScore = roomResults.values().stream()
                .mapToInt(this::calculateScore)
                .average()
                .orElse(0.0);

        return (int) Math.round(averageScore);
    }

    private int calculateScore(HealthCheckResponseDtoV1 dto) {
        if (dto == null) {
            log.debug("[HealthScore] Response is null - gateway error");
            return 0;
        }

        if (dto.getStatus() != 200) {
            log.debug("[HealthScore] Response status {} != 200: {}", dto.getStatus(), dto.getMessage());
            return 0;
        }

        if (dto.getData() == null) {
            log.debug("[HealthScore] Response data is null - invalid response format");
            return 0;
        }

        List<DeviceDto> devices = dto.getData().getDevices();
        
        if (devices == null) {
            log.debug("[HealthScore] Devices list is null - response format issue");
            return 0;
        }

        if (devices.isEmpty()) {
            log.debug("[HealthScore] Room has no devices - empty room");
            return 100;
        }

        long activeCount = devices.stream().filter(DeviceDto::isActive).count();
        int score = (int) (((double) activeCount / devices.size()) * 100);
        log.debug("[HealthScore] Calculated score: {}/{} devices active = {}%", 
                activeCount, devices.size(), score);
        return score;
    }
}
