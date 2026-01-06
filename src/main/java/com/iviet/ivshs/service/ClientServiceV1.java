package com.iviet.ivshs.service;

import com.iviet.ivshs.entities.Client;
import com.iviet.ivshs.dto.ClientDtoV1;
import com.iviet.ivshs.dto.CreateClientDtoV1;
import com.iviet.ivshs.dto.PaginatedResponseV1;
import com.iviet.ivshs.dto.UpdateClientDtoV1;

public interface ClientServiceV1 {

    PaginatedResponseV1<ClientDtoV1> getAll(int page, int size);

    ClientDtoV1 getById(Long clientId);

    Client getEntityById(Long clientId);

    ClientDtoV1 getByUsername(String username);

    Client getEntityByUsername(String username);

    ClientDtoV1 getUserById(Long userId);

    ClientDtoV1 getGatewayById(Long gatewayId);

    ClientDtoV1 getUserByUsername(String username);

    ClientDtoV1 getUserByIpAddress(String ipAddress);

    ClientDtoV1 getGatewayByUsername(String username);

    ClientDtoV1 getGatewayByIpAddress(String ipAddress);

    ClientDtoV1 create(CreateClientDtoV1 createDto);

    ClientDtoV1 update(Long clientId, UpdateClientDtoV1 updateDto);

    void delete(Long clientId);

    PaginatedResponseV1<ClientDtoV1> getAllGatewaysByRoomId(Long roomId, int page, int size);
    
    Long countGateWayByRoomId(Long roomId);

    boolean existsByUsername(String username);
}
