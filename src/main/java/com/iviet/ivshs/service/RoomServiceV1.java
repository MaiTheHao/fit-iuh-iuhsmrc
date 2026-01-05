package com.iviet.ivshs.service;

import com.iviet.ivshs.dto.CreateRoomDtoV1;
import com.iviet.ivshs.dto.RoomDtoV1;
import com.iviet.ivshs.dto.PaginatedResponseV1;
import com.iviet.ivshs.dto.UpdateRoomDtoV1;
import com.iviet.ivshs.entities.RoomV1;

public interface RoomServiceV1 {
    PaginatedResponseV1<RoomDtoV1> getListByFloor(Long floorId, int page, int size);
    RoomDtoV1 getById(Long roomId);
    RoomV1 getEntityById(Long roomId);
    RoomDtoV1 getByCode(String roomCode);
    RoomV1 getEntityByCode(String roomCode);
    RoomDtoV1 create(Long floorId, CreateRoomDtoV1 dto);
    RoomDtoV1 update(Long roomId, UpdateRoomDtoV1 dto);
    void delete(Long roomId);
}
