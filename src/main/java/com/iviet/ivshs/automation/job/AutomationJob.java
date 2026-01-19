package com.iviet.ivshs.automation.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iviet.ivshs.service.AutomationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AutomationJob implements Job {
    
    @Autowired
    private AutomationService automationService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long automationId = context.getJobDetail().getJobDataMap().getLong("id");
        long start = System.currentTimeMillis();
        
        log.info("[AUTOMATION-JOB] Starting execution for ID: {}", automationId);
        
        try {
            automationService.executeAutomationLogic(automationId);
            log.info("[AUTOMATION-JOB] Finished execution for ID: {} in {}ms", 
                    automationId, System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("[AUTOMATION-JOB] Execution failed for ID {}: {}", automationId, e.getMessage());
            throw new JobExecutionException(e.getMessage(), e);
        }
    }
}