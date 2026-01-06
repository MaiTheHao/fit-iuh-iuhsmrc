package com.iviet.ivshs.service;

import java.time.Instant;
import java.util.List;

import com.iviet.ivshs.dto.AverageTemperatureValueDtoV1;
import com.iviet.ivshs.dto.CreateTemperatureValueDtoV1;
import com.iviet.ivshs.entities.Temperature;

public interface TemperatureValueServiceV1 {
	List<AverageTemperatureValueDtoV1> getAverageTemperatureByRoom(Long roomId, Instant fromTimestamp, Instant toTimestamp);
	void create(CreateTemperatureValueDtoV1 dto);
	void createWithSensor(Temperature sensor, CreateTemperatureValueDtoV1 dto);
	void createBatchWithSensor(Temperature sensor, List<CreateTemperatureValueDtoV1> dtoList);
}