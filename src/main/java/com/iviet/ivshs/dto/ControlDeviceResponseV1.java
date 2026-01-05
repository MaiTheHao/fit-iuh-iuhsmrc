package com.iviet.ivshs.dto;

import java.time.Instant;

import lombok.Builder;

@Builder
public record ControlDeviceResponseV1 (
	Integer status,
	String message,
	String data,
	Instant timestamp 
){}