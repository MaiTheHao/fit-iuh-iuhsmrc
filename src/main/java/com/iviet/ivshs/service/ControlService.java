package com.iviet.ivshs.service;

import com.iviet.ivshs.dto.ControlDeviceResponse;
import com.iviet.ivshs.enumeration.GatewayCommand;

public interface ControlService {
	ControlDeviceResponse sendCommand(String gatewayIp, String targetNaturalId, GatewayCommand command);
}
