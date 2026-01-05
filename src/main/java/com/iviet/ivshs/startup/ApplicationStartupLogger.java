package com.iviet.ivshs.startup;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "StartupLogger")
@Component
@Order(1)
public class ApplicationStartupLogger implements ApplicationListener<ContextRefreshedEvent> {

    private boolean isLogged = false;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (isLogged) {
            return;
        }

        log.info("🚀 SMART ROOM IOT SYSTEM :: STARTING UP");
        log.info("────────────────────────────────────────────────────────────");
        log.info("📋 System Configuration:");
        log.info("   🏗️  Context  : {}", event.getApplicationContext().getDisplayName());
        log.info("   🔧  Profiles : {}", String.join(", ", event.getApplicationContext().getEnvironment().getActiveProfiles()));
        log.info("   📦  Beans    : {} definitions loaded", event.getApplicationContext().getBeanDefinitionCount());
        log.info("────────────────────────────────────────────────────────────");
        log.info("⚙️  Initializing startup sequences...");
        log.info("");

        isLogged = true;
    }
}
