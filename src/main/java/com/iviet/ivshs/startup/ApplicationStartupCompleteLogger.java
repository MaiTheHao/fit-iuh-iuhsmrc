package com.iviet.ivshs.startup;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "StartupLogger")
@Component
@Order(999)
public class ApplicationStartupCompleteLogger implements ApplicationListener<ContextRefreshedEvent> {

    private boolean isLogged = false;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (isLogged) {
            return;
        }

        log.info("");
        log.info("✨ SERVER IS READY");
        log.info("────────────────────────────────────────────────────────────");
        log.info("   🟢 Status : All systems operational");
        log.info("   🌐 URL    : http://localhost:{}", event.getApplicationContext().getEnvironment().getProperty("server.port", "8080"));
        log.info("   🕒 Time   : {}", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("────────────────────────────────────────────────────────────");
        log.info("🚀 Application is now listening for requests");
        log.info("");

        isLogged = true;
    }
}
