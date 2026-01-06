package com.iviet.ivshs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.iviet.ivshs.dto.PowerConsumptionDtoV1;
import com.iviet.ivshs.dto.CreatePowerConsumptionDtoV1;
import com.iviet.ivshs.dto.UpdatePowerConsumptionDtoV1;
import com.iviet.ivshs.entities.PowerConsumption;
import com.iviet.ivshs.entities.PowerConsumptionLan;
import com.iviet.ivshs.annotation.IgnoreAuditFields;

@Mapper(componentModel = "spring")
public interface PowerConsumptionMapperV1 {

	@Mapping(target = "id", source = "entity.id")
	@Mapping(target = "name", source = "sensorLan.name")
	@Mapping(target = "description", source = "sensorLan.description")
	@Mapping(target = "isActive", source = "entity.isActive")
	@Mapping(target = "currentWatt", source = "entity.currentWatt")
	@Mapping(target = "roomId", source = "entity.room.id")
	PowerConsumptionDtoV1 toDto(PowerConsumption entity, PowerConsumptionLan sensorLan);

	@IgnoreAuditFields
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "translations", ignore = true)
	@Mapping(target = "consumptionValues", ignore = true)
	@Mapping(target = "deviceControl", ignore = true)
	@Mapping(target = "room", ignore = true)
	PowerConsumption toEntity(PowerConsumptionDtoV1 dto);

	@IgnoreAuditFields
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "translations", ignore = true)
	@Mapping(target = "currentWatt", ignore = true)
	@Mapping(target = "consumptionValues", ignore = true)
	@Mapping(target = "deviceControl", ignore = true)
	@Mapping(target = "room", ignore = true)
	PowerConsumption fromCreateDto(CreatePowerConsumptionDtoV1 dto);

	@IgnoreAuditFields
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "translations", ignore = true)
	@Mapping(target = "currentWatt", ignore = true)
	@Mapping(target = "consumptionValues", ignore = true)
	@Mapping(target = "deviceControl", ignore = true)
	@Mapping(target = "room", ignore = true)
	PowerConsumption fromUpdateDto(UpdatePowerConsumptionDtoV1 dto);
}
