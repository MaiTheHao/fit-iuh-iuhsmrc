package com.iviet.ivshs.automation.handler.impl;

import org.springframework.stereotype.Component;

import com.iviet.ivshs.automation.handler.AutomationActionHandler;
import com.iviet.ivshs.entities.AutomationAction;
import com.iviet.ivshs.enumeration.JobActionType;
import com.iviet.ivshs.enumeration.JobTargetType;
import com.iviet.ivshs.service.LightService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LightAutomationActionHandler implements AutomationActionHandler {

    private final LightService lightService;

    @Override
    public JobTargetType getTargetType() {
        return JobTargetType.LIGHT;
    }

    @Override
    public void handle(AutomationAction action) throws Exception {
        log.info("Processing LIGHT action: ID={}, Action={}", action.getTargetId(), action.getActionType());

        boolean newState = (action.getActionType() == JobActionType.ON);

        try {
            lightService.handleStateControl(action.getTargetId(), newState);
            log.info("Light {} -> {}", action.getTargetId(), newState ? "ON" : "OFF");
        } catch (Exception e) {
            log.error("Failed to execute light command [DeviceID: {}]. Error: {}", action.getTargetId(), e.getMessage());
            throw e;
        }
    }
}