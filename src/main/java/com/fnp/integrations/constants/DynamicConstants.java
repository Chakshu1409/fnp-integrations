package com.fnp.integrations.constants;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
@Configuration
public class DynamicConstants {

    // Application Configuration
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.application.name}")
    private String applicationName;

    // Feature Flags
    @Value("${feature.debug-mode}")
    private boolean debugMode;
    
    @Value("${feature.cache-enabled}")
    private boolean cacheEnabled;

    // API Configuration
    @Value("${api.base-url}")
    private String apiBaseUrl;
    
    @Value("${api.timeout}")
    private int apiTimeout;
    
    @Value("${api.retry-count}")
    private int apiRetryCount;

    // Security Configuration (only in UAT and PROD)
    @Value("${security.enabled:false}")
    private boolean securityEnabled;
    
    @Value("${security.jwt.expiration:3600000}")
    private long jwtExpiration;

    // Server Configuration
    @Value("${server.port}")
    private int serverPort;
    
    @Value("${server.servlet.context-path}")
    private String contextPath;
}