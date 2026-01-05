package com.iviet.ivshs.service;

import com.iviet.ivshs.dto.CreateTemperatureDtoV1;
import com.iviet.ivshs.dto.PaginatedResponseV1;
import com.iviet.ivshs.dto.TemperatureDtoV1;
import com.iviet.ivshs.dto.UpdateTemperatureDtoV1;
import com.iviet.ivshs.entities.TemperatureV1;

public interface TemperatureServiceV1 {

    PaginatedResponseV1<TemperatureDtoV1> getListByRoom(Long roomId, int page, int size);

    PaginatedResponseV1<TemperatureV1> getListEntityByRoom(Long roomId, int page, int size);

    TemperatureDtoV1 getById(Long tempSensorId);

    TemperatureV1 getEntityById(Long tempSensorId);

    TemperatureDtoV1 getByNaturalId(String naturalId);

    TemperatureV1 getEntityByNaturalId(String naturalId);

    TemperatureDtoV1 create(CreateTemperatureDtoV1 dto);

    TemperatureDtoV1 update(Long tempSensorId, UpdateTemperatureDtoV1 dto);

    void delete(Long tempSensorId);
}
