package com.iviet.ivshs.automation.processor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.iviet.ivshs.automation.handler.AutomationActionHandler;
import com.iviet.ivshs.entities.Automation;
import com.iviet.ivshs.entities.AutomationAction;
import com.iviet.ivshs.enumeration.JobTargetType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AutomationProcessor {

    private final Map<JobTargetType, AutomationActionHandler> actionHandlerMap;

    public AutomationProcessor(List<AutomationActionHandler> actionHandlers) {
        this.actionHandlerMap = actionHandlers.stream()
                .collect(Collectors.toMap(AutomationActionHandler::getTargetType, Function.identity()));
        
        log.info("[AUTOMATION] Processor initialized with {} handlers: {}", 
                actionHandlerMap.size(), actionHandlerMap.keySet());
    }

    @Transactional
    public void process(Automation automation) {
        long start = System.currentTimeMillis();
        log.info("[AUTOMATION] Starting process: {} [ID: {}]", automation.getName(), automation.getId());

        if (automation.getActions() == null || automation.getActions().isEmpty()) {
            log.warn("[AUTOMATION] Automation [ID: {}] has no actions defined", automation.getId());
            return;
        }

        automation.getActions().stream()
                .sorted(Comparator.comparingInt(AutomationAction::getExecutionOrder))
                .forEach(this::dispatchAction);

        log.info("[AUTOMATION] Finished process: {} [ID: {}] in {}ms", 
                automation.getName(), automation.getId(), System.currentTimeMillis() - start);
    }

    private void dispatchAction(AutomationAction action) {
        long actionStart = System.currentTimeMillis();
        JobTargetType type = action.getTargetType();
        AutomationActionHandler handler = actionHandlerMap.get(type);

        if (handler == null) {
            log.error("[ACTION] No handler registered for TargetType: {}. Action [ID: {}] skipped", type, action.getId());
            return;
        }

        try {
            log.info("[ACTION] Executing action [ID: {}, Type: {}]", action.getId(), type);
            handler.handle(action);
            log.info("[ACTION] Success: [ID: {}, Type: {}] in {}ms", action.getId(), type, System.currentTimeMillis() - actionStart);
        } catch (Exception e) {
            log.error("[ACTION] Execution failed [ID: {}, Type: {}]: {}", action.getId(), type, e.getMessage());
        }
    }
}