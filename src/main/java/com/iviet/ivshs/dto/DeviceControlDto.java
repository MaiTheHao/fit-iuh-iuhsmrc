package com.iviet.ivshs.dto;

import com.iviet.ivshs.enumeration.DeviceControlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceControlDto {
    
    private Long id;
    private DeviceControlType deviceControlType;
    private Integer gpioPin;
    private String bleMacAddress;
    private String apiEndpoint;
    private Long clientId;
    private Long roomId;
}
