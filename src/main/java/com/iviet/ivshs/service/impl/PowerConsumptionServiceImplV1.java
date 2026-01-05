package com.iviet.ivshs.service.impl;

import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iviet.ivshs.dao.DeviceControlDaoV1;
import com.iviet.ivshs.dao.LanguageDaoV1;
import com.iviet.ivshs.dao.PowerConsumptionDaoV1;
import com.iviet.ivshs.dao.RoomDaoV1;
import com.iviet.ivshs.dto.CreatePowerConsumptionDtoV1;
import com.iviet.ivshs.dto.PaginatedResponseV1;
import com.iviet.ivshs.dto.PowerConsumptionDtoV1;
import com.iviet.ivshs.dto.UpdatePowerConsumptionDtoV1;
import com.iviet.ivshs.entities.DeviceControlV1;
import com.iviet.ivshs.entities.PowerConsumptionLanV1;
import com.iviet.ivshs.entities.PowerConsumptionV1;
import com.iviet.ivshs.entities.RoomV1;
import com.iviet.ivshs.exception.domain.BadRequestException;
import com.iviet.ivshs.exception.domain.NotFoundException;
import com.iviet.ivshs.mapper.PowerConsumptionMapperV1;
import com.iviet.ivshs.service.PowerConsumptionServiceV1;
import com.iviet.ivshs.util.LocalContextUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PowerConsumptionServiceImplV1 implements PowerConsumptionServiceV1 {

	private final PowerConsumptionDaoV1 powerConsumptionDao;
	private final LanguageDaoV1 languageDao;
	private final RoomDaoV1 RoomDaoV1;
	private final DeviceControlDaoV1 deviceControlDao;
	private final PowerConsumptionMapperV1 powerConsumptionMapper;

	@Override
	@Transactional(readOnly = true)
	public PaginatedResponseV1<PowerConsumptionDtoV1> getListByRoom(Long roomId, int page, int size) {
		if (roomId == null) {
			throw new BadRequestException("Room ID is required");
		}

		List<PowerConsumptionDtoV1> data = powerConsumptionDao.findAllByRoomId(roomId, page, size, LocaleContextHolder.getLocale().getLanguage());
		Long totalElements = powerConsumptionDao.countByRoomId(roomId);

		return new PaginatedResponseV1<>(data, page, size, totalElements);
	}

	@Override
	@Transactional(readOnly = true)
	public PaginatedResponseV1<PowerConsumptionV1> getListEntityByRoom(Long roomId, int page, int size) {
		if (roomId == null) throw new BadRequestException("Room ID is required");
		
		List<PowerConsumptionV1> data = powerConsumptionDao.findAllByRoomId(roomId, page, size);
		Long totalElements = powerConsumptionDao.countByRoomId(roomId);

		return new PaginatedResponseV1<>(data, page, size, totalElements);
	}

	@Override
	@Transactional(readOnly = true)
	public PowerConsumptionDtoV1 getById(Long powerSensorId) {
		if (powerSensorId == null) {
			throw new BadRequestException("Power sensor ID is required");
		}

		String langCode = LocaleContextHolder.getLocale().getLanguage();
		PowerConsumptionDtoV1 dto = powerConsumptionDao.findById(powerSensorId, langCode).orElseThrow(
			() -> new NotFoundException("Power sensor not found with ID: " + powerSensorId)
		);

		return dto;
	}

	@Override
	@Transactional(readOnly = true) 
	public PowerConsumptionV1 getEntityById(Long powerSensorId) {
		if (powerSensorId == null) throw new BadRequestException("Power sensor ID is required");
		return powerConsumptionDao.findById(powerSensorId).orElseThrow(() -> new NotFoundException("Power sensor not found"));
	}

	@Override
	@Transactional(readOnly = true)
	public PowerConsumptionDtoV1 getByNaturalId(String naturalId) {
		if (naturalId.isBlank()) throw new BadRequestException("Natural ID is required");

		String langCode = LocalContextUtil.getCurrentLangCode();
		PowerConsumptionDtoV1 dto = powerConsumptionDao.findByNaturalId(naturalId, langCode)
			.orElseThrow(() -> new NotFoundException("Power sensor not found with natural ID: " + naturalId));

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public PowerConsumptionV1 getEntityByNaturalId(String naturalId) {
		if (naturalId.isBlank()) throw new BadRequestException("Natural ID is required");

		return powerConsumptionDao.findByNaturalId(naturalId)
			.orElseThrow(() -> new NotFoundException("Power sensor not found with natural ID: " + naturalId));
	}

	@Override
	@Transactional
	public PowerConsumptionDtoV1 create(CreatePowerConsumptionDtoV1 dto) {
		if (dto == null) {
			throw new BadRequestException("Power consumption data is required");
		}
		if (dto.getRoomId() == null) {
			throw new BadRequestException("Room ID is required");
		}
		if (dto.getDeviceControlId() == null) {
			throw new BadRequestException("Device Control ID is required");
		}

		RoomV1 room = RoomDaoV1.findById(dto.getRoomId()).orElseThrow(() -> new NotFoundException("Room not found"));

		DeviceControlV1 deviceControl = deviceControlDao.findById(dto.getDeviceControlId()).orElseThrow(() -> new NotFoundException("Device Control not found, cannot create power sensor"));
		String langCode = dto.getLangCode() == null ?
			LocaleContextHolder.getLocale().getLanguage() : dto.getLangCode();
		if (!languageDao.existsByCode(langCode)) {
			throw new BadRequestException("Language with code " + langCode + " not found");
		}

		PowerConsumptionV1 powerConsumption = powerConsumptionMapper.fromCreateDto(dto);
		powerConsumption.setRoom(room);
		powerConsumption.setDeviceControl(deviceControl);

		PowerConsumptionLanV1 sensorLan = new PowerConsumptionLanV1();
		sensorLan.setLangCode(langCode);
		sensorLan.setName(dto.getName());
		sensorLan.setDescription(dto.getDescription());
		sensorLan.setOwner(powerConsumption);

		powerConsumption.getTranslations().add(sensorLan);

		powerConsumptionDao.save(powerConsumption);

		return powerConsumptionDao.findById(powerConsumption.getId(), langCode).orElseThrow(
			() -> new NotFoundException("Power sensor not found with ID: " + powerConsumption.getId())
		);
	}

	@Override
	@Transactional
	public PowerConsumptionDtoV1 update(Long powerSensorId, UpdatePowerConsumptionDtoV1 dto) {
		if (powerSensorId == null) {
			throw new BadRequestException("Power sensor ID is required");
		}
		if (dto == null) {
			throw new BadRequestException("Update data is required");
		}

		PowerConsumptionV1 powerConsumption = powerConsumptionDao.findById(powerSensorId).orElseThrow(() -> new NotFoundException("Power sensor not found"));

		String langCode = dto.getLangCode() == null ?
			LocaleContextHolder.getLocale().getLanguage() : dto.getLangCode();
		if (!languageDao.existsByCode(langCode)) {
			throw new BadRequestException("Language with code " + langCode + " not found");
		}

		PowerConsumptionLanV1 sensorLan = powerConsumption.getTranslations().stream()
				.filter(lan -> lan.getLangCode().equals(langCode))
				.findFirst()
				.orElse(null);

		if (sensorLan == null) {
			sensorLan = new PowerConsumptionLanV1();
			sensorLan.setLangCode(langCode);
			sensorLan.setOwner(powerConsumption);
			powerConsumption.getTranslations().add(sensorLan);
		}

		if (dto.getName() != null) {
			sensorLan.setName(dto.getName());
		}

		if (dto.getDescription() != null) {
			sensorLan.setDescription(dto.getDescription());
		}

		if (dto.getIsActive() != null) {
			powerConsumption.setIsActive(dto.getIsActive());
		}

		if (dto.getNaturalId() != null) {
			powerConsumption.setNaturalId(dto.getNaturalId());
		}

		if (dto.getDeviceControlId() != null) {
			DeviceControlV1 deviceControl = deviceControlDao.findById(dto.getDeviceControlId()).orElseThrow(() -> new NotFoundException("Device Control not found"));
			powerConsumption.setDeviceControl(deviceControl);
		}

		powerConsumptionDao.update(powerConsumption);

		return powerConsumptionDao.findById(powerSensorId,LocaleContextHolder.getLocale().getLanguage()).orElseThrow(
				() -> new NotFoundException("Power sensor not found with ID: " + powerSensorId)
			);
	}

	@Override
	@Transactional
	public void delete(Long powerSensorId) {
		if (powerSensorId == null) {
			throw new BadRequestException("Power sensor ID is required");
		}

		PowerConsumptionV1 powerConsumption = powerConsumptionDao.findById(powerSensorId).orElseThrow(() -> new NotFoundException("Power sensor not found"));
		

		powerConsumptionDao.delete(powerConsumption);
	}
}
