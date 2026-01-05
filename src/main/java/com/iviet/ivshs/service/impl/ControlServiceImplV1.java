package com.iviet.ivshs.service.impl;

import com.iviet.ivshs.constant.UrlConstant;
import com.iviet.ivshs.dto.ControlDeviceRequestV1;
import com.iviet.ivshs.dto.ControlDeviceResponseV1;
import com.iviet.ivshs.enumeration.GatewayCommandV1;
import com.iviet.ivshs.exception.domain.BadRequestException;
import com.iviet.ivshs.service.ControlServiceV1;
import com.iviet.ivshs.util.HttpClientUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ControlServiceImplV1 implements ControlServiceV1 {
	
	@Override
	public ControlDeviceResponseV1 sendCommand(String gatewayIp, String targetNaturalId, GatewayCommandV1 command) {
		if (gatewayIp == null || gatewayIp.isEmpty()) throw new BadRequestException("Gateway IP is required");
		
		if (targetNaturalId == null || targetNaturalId.isEmpty()) throw new BadRequestException("Target Natural ID is required");
		
		if (command == null) throw new BadRequestException("Command is required");

		String url = UrlConstant.getControlUrlV1(gatewayIp, targetNaturalId);
		ControlDeviceRequestV1 requestBody = ControlDeviceRequestV1.builder()
				.command(command)
				.build();
		
		try {
			log.info("[CONTROL] Sending CMD [{}] to Device [{}] at IP [{}]", command, targetNaturalId, gatewayIp);
			HttpClientUtil.Response response = HttpClientUtil.post(url, requestBody);
			return HttpClientUtil.fromJson(response.getBody(), ControlDeviceResponseV1.class);
		} catch (Exception e) {
			log.error("[CONTROL] Error while sending command: {}", e.getMessage(), e);
			throw e;
		}
	}
}