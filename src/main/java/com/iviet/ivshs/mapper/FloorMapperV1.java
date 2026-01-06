package com.iviet.ivshs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.iviet.ivshs.dto.CreateFloorDtoV1;
import com.iviet.ivshs.dto.FloorDtoV1;
import com.iviet.ivshs.entities.FloorLan;
import com.iviet.ivshs.entities.Floor;
import com.iviet.ivshs.annotation.IgnoreAuditFields;

@Mapper(componentModel = "spring")
public interface FloorMapperV1 {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "code", source = "entity.code")
    @Mapping(target = "name", source = "floorLan.name")
    @Mapping(target = "description", source = "floorLan.description")
    @Mapping(target = "level", source = "entity.level")
    FloorDtoV1 toDto(Floor entity, FloorLan floorLan);

    @IgnoreAuditFields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "translations", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    Floor toEntity(FloorDtoV1 dto);

    @IgnoreAuditFields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "translations", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    Floor fromCreateDto(CreateFloorDtoV1 dto);
}
