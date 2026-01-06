package com.iviet.ivshs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.iviet.ivshs.annotation.IgnoreAuditFields;
import com.iviet.ivshs.dto.CreateSysFunctionDtoV1;
import com.iviet.ivshs.dto.SysFunctionDtoV1;
import com.iviet.ivshs.dto.UpdateSysFunctionDtoV1;
import com.iviet.ivshs.entities.SysFunctionLan;
import com.iviet.ivshs.entities.SysFunction;

@Mapper(componentModel = "spring")
public interface SysFunctionMapperV1 {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "functionCode", source = "entity.functionCode")
    @Mapping(target = "name", source = "functionLan.name")
    @Mapping(target = "description", source = "functionLan.description")
    SysFunctionDtoV1 toDto(SysFunction entity, SysFunctionLan functionLan);

    @IgnoreAuditFields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "translations", ignore = true)
    @Mapping(target = "roles", ignore = true)
    SysFunction toEntity(SysFunctionDtoV1 dto);

    @IgnoreAuditFields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "translations", ignore = true)
    @Mapping(target = "roles", ignore = true)
    SysFunction fromCreateDto(CreateSysFunctionDtoV1 dto);

    @IgnoreAuditFields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "translations", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "functionCode", ignore = true)
    SysFunction fromUpdateDto(UpdateSysFunctionDtoV1 dto);
}
