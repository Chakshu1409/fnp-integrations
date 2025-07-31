package com.fnp.integrations.controller;

import com.fnp.integrations.constants.DynamicConstants;
import com.fnp.integrations.dto.ResponseDto;
import com.fnp.integrations.exception.ResponseException;
import com.fnp.integrations.service.ExternalApiService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class ConfigController {

    private final DynamicConstants dynamicConstants;
    private final ExternalApiService externalApiService;

    @GetMapping("/info")
    public ResponseDto<Map<String, Object>> getConfigInfo() {
        Map<String, Object> config = new HashMap<>();
        config.put("applicationName", dynamicConstants.getApplicationName());
        config.put("activeProfile", dynamicConstants.getActiveProfile());
        config.put("debugMode", dynamicConstants.isDebugMode());
        config.put("cacheEnabled", dynamicConstants.isCacheEnabled());
        config.put("apiBaseUrl", dynamicConstants.getApiBaseUrl());
        config.put("apiTimeout", dynamicConstants.getApiTimeout());
        config.put("apiRetryCount", dynamicConstants.getApiRetryCount());
        config.put("securityEnabled", dynamicConstants.isSecurityEnabled());
        config.put("jwtExpiration", dynamicConstants.getJwtExpiration());
        config.put("serverPort", dynamicConstants.getServerPort());
        config.put("contextPath", dynamicConstants.getContextPath());
        return ResponseDto.success("Configuration information retrieved successfully", config);
    }

    @GetMapping("/health")
    public ResponseDto<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("profile", dynamicConstants.getActiveProfile());
        health.put("application", dynamicConstants.getApplicationName());
        health.put("serverPort", String.valueOf(dynamicConstants.getServerPort()));
        return ResponseDto.success("Application is healthy", health);
    }

    @GetMapping("/test-error")
    public ResponseDto<String> testError(@RequestParam(required = false) String type) {
        if ("response".equals(type)) {
            throw new ResponseException("This is a test ResponseException with 501 status code");
        } else if ("custom".equals(type)) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("customField", "customValue");
            errorData.put("timestamp", System.currentTimeMillis());
            throw new ResponseException("Custom error with data", errorData);
        } else if ("bad-request".equals(type)) {
            throw new IllegalArgumentException("This is a bad request error");
        } else {
            throw new RuntimeException("This is a generic runtime exception");
        }
    }

    @GetMapping("/test-rest-client")
    public ResponseDto<Map<String, Object>> testRestClient(@RequestParam(required = false) String endpoint) {
        try {
            String testEndpoint = endpoint != null ? endpoint : "/test";
            Map<String, Object> result = externalApiService.getExternalDataSafe(testEndpoint);
            
            Map<String, Object> response = new HashMap<>();
            response.put("endpoint", testEndpoint);
            response.put("result", result);
            response.put("apiBaseUrl", dynamicConstants.getApiBaseUrl());
            
            return ResponseDto.success("REST client test completed successfully", response);
        } catch (Exception e) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("error", e.getMessage());
            errorData.put("endpoint", endpoint);
            return ResponseDto.error(500, 500, "REST client test failed", errorData);
        }
    }
} 