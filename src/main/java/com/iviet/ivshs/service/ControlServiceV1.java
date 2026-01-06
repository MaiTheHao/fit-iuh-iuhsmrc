package com.iviet.ivshs.service;

import com.iviet.ivshs.dto.ControlDeviceResponse;
import com.iviet.ivshs.enumeration.GatewayCommand;

public interface ControlServiceV1 {
	ControlDeviceResponse sendCommand(String gatewayIp, String targetNaturalId, GatewayCommand command);
}
