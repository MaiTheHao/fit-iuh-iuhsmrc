package com.iviet.ivshs.service;

import com.iviet.ivshs.dto.ControlDeviceResponseV1;
import com.iviet.ivshs.enumeration.GatewayCommandV1;

public interface ControlServiceV1 {
	ControlDeviceResponseV1 sendCommand(String gatewayIp, String targetNaturalId, GatewayCommandV1 command);
}
