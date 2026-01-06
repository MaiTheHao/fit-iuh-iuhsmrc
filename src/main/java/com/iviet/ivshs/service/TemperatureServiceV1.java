package com.iviet.ivshs.service;

import com.iviet.ivshs.dto.CreateTemperatureDtoV1;
import com.iviet.ivshs.dto.PaginatedResponseV1;
import com.iviet.ivshs.dto.TemperatureDtoV1;
import com.iviet.ivshs.dto.UpdateTemperatureDtoV1;
import com.iviet.ivshs.entities.Temperature;

public interface TemperatureServiceV1 {

    PaginatedResponseV1<TemperatureDtoV1> getListByRoom(Long roomId, int page, int size);

    PaginatedResponseV1<Temperature> getListEntityByRoom(Long roomId, int page, int size);

    TemperatureDtoV1 getById(Long tempSensorId);

    Temperature getEntityById(Long tempSensorId);

    TemperatureDtoV1 getByNaturalId(String naturalId);

    Temperature getEntityByNaturalId(String naturalId);

    TemperatureDtoV1 create(CreateTemperatureDtoV1 dto);

    TemperatureDtoV1 update(Long tempSensorId, UpdateTemperatureDtoV1 dto);

    void delete(Long tempSensorId);
}
