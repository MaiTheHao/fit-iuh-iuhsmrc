package com.iviet.ivshs.dto;

import com.iviet.ivshs.enumeration.AcMode;
import com.iviet.ivshs.enumeration.AcPower;
import com.iviet.ivshs.enumeration.AcSwing;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateAirConditionDto(
    @NotBlank(message = "Device name is required")
    @Size(min = 1, max = 100)
    String name,

    @NotBlank(message = "Natural ID is required")
    String naturalId,

    @Size(max = 255)
    String description,

    Boolean isActive,

    @NotNull(message = "Room ID is required")
    Long roomId,

    Long deviceControlId,

    @Size(max = 10)
    String langCode,

    AcPower power,

    @Min(16) @Max(32)
    Integer temperature,

    AcMode mode,

    @Min(1) @Max(5)
    Integer fanSpeed,

    AcSwing swing
) {}