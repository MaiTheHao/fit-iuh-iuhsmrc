package com.iviet.ivshs.mapper;

import com.iviet.ivshs.dto.CreateTemperatureValueDtoV1;
import com.iviet.ivshs.dto.TemperatureValueDtoV1;
import com.iviet.ivshs.entities.TemperatureValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TemperatureValueMapperV1 {

    @Mapping(target = "sensorId", source = "entity.sensor.id")
    TemperatureValueDtoV1 toDto(TemperatureValue entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sensor", ignore = true)
    TemperatureValue fromCreateDto(CreateTemperatureValueDtoV1 dto);
}