package com.dust.courseRegistration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dust.courseRegistration.service.InitAppService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ApplicationInitConfig {

    private final InitAppService initAppService;
    private final String ADMIN_USER_NAME;
    private final String ADMIN_PASSWORD;

    public ApplicationInitConfig(
            InitAppService initAppService,
            @Value("${default.admin.username}") String defaultAdminUsername,
            @Value("${default.admin.password}") String defaultAdminPassword) {
        this.initAppService = initAppService;
        this.ADMIN_USER_NAME = defaultAdminUsername;
        this.ADMIN_PASSWORD = defaultAdminPassword;
    }

    @Bean
    ApplicationRunner applicationRunner() {
        log.info("Initializing application...");
        return args -> {
            initAppService.initializeApplication(ADMIN_USER_NAME, ADMIN_PASSWORD);
            log.info("Application initialization completed.");
        };
    }
}
