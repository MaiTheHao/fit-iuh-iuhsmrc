package com.iviet.ivshs.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.iviet.ivshs.dto.CreateLanguageDtoV1;
import com.iviet.ivshs.dto.LanguageDtoV1;
import com.iviet.ivshs.dto.UpdateLanguageDtoV1;
import com.iviet.ivshs.entities.Language;
import com.iviet.ivshs.annotation.IgnoreAuditFields;

@Mapper(componentModel = "spring")
public interface LanguageMapperV1 {
    
    LanguageDtoV1 toDto(Language entity);

    @IgnoreAuditFields
    @Mapping(target = "id", ignore = true)
    Language toEntity(LanguageDtoV1 dto);

    @IgnoreAuditFields
    @Mapping(target = "id", ignore = true)
    Language fromCreateDto(CreateLanguageDtoV1 dto);

    @IgnoreAuditFields
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(@MappingTarget Language entity, UpdateLanguageDtoV1 dto);
}
