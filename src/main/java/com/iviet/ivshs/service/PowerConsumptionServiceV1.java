package com.iviet.ivshs.service;

import com.iviet.ivshs.dto.CreatePowerConsumptionDtoV1;
import com.iviet.ivshs.dto.PaginatedResponseV1;
import com.iviet.ivshs.dto.PowerConsumptionDtoV1;
import com.iviet.ivshs.dto.UpdatePowerConsumptionDtoV1;
import com.iviet.ivshs.entities.PowerConsumption;

public interface PowerConsumptionServiceV1 {

    PaginatedResponseV1<PowerConsumptionDtoV1> getListByRoom(Long roomId, int page, int size);

    PaginatedResponseV1<PowerConsumption> getListEntityByRoom(Long roomId, int page, int size);

    PowerConsumptionDtoV1 getById(Long powerSensorId);

    PowerConsumption getEntityById(Long powerSensorId);

    PowerConsumptionDtoV1 getByNaturalId(String naturalId);

    PowerConsumption getEntityByNaturalId(String naturalId);

    PowerConsumptionDtoV1 create(CreatePowerConsumptionDtoV1 dto);

    PowerConsumptionDtoV1 update(Long powerSensorId, UpdatePowerConsumptionDtoV1 dto);

    void delete(Long powerSensorId);
}
