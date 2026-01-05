package com.iviet.ivshs.service;

import com.iviet.ivshs.dto.HealthCheckResponseDtoV1;
import java.util.Map;

public interface HealthCheckServiceV1 {
    HealthCheckResponseDtoV1 checkByClient(String ipAddress);
    HealthCheckResponseDtoV1 checkByClient(Long clientId);

    Map<String, HealthCheckResponseDtoV1> checkByRoom(String roomCode);
    Map<String, HealthCheckResponseDtoV1> checkByRoom(Long roomId);

    int getHealthScoreByClient(Long clientId);
    int getHealthScoreByRoom(Long roomId);
}