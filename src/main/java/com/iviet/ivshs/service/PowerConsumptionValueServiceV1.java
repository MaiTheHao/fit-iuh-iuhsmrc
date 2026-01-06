package com.iviet.ivshs.service;

import java.time.Instant;
import java.util.List;

import com.iviet.ivshs.dto.CreatePowerConsumptionValueDtoV1;
import com.iviet.ivshs.dto.SumPowerConsumptionValueDtoV1;
import com.iviet.ivshs.entities.PowerConsumption;

public interface PowerConsumptionValueServiceV1 {
	List<SumPowerConsumptionValueDtoV1> getSumPowerConsumptionByRoom(Long roomId, Instant fromTimestamp, Instant toTimestamp);
	void create(CreatePowerConsumptionValueDtoV1 dto);
	void createWithSensor(PowerConsumption sensor, CreatePowerConsumptionValueDtoV1 dto);
	void createBatchWithSensor(PowerConsumption sensor, List<CreatePowerConsumptionValueDtoV1> dtoList);
}
