package com.fnp.integrations.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    
    // Success
    SUCCESS(200, "Success"),
    
    // Client Errors
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),
    CONFLICT(409, "Conflict"),
    VALIDATION_ERROR(422, "Validation Error"),
    
    // Server Errors
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    
    // Microservice Integration Errors
    MICROSERVICE_INTERNAL_ERROR(500, "Microservice Internal Error"),
    MICROSERVICE_EXCHANGE_ERROR(502, "Microservice Exchange Error"),
    MICROSERVICE_AUTHORIZATION_ERROR(401, "Microservice Authorization Error"),
    MICROSERVICE_RESOURCE_NOT_FOUND(404, "Microservice Resource Not Found"),
    MICROSERVICE_TIMEOUT_ERROR(504, "Microservice Timeout Error"),
    
    // Integration Specific Errors
    INTEGRATION_FAILED(501, "Integration Failed"),
    EXTERNAL_API_ERROR(502, "External API Error"),
    INVALID_RESPONSE_FORMAT(422, "Invalid Response Format"),
    RATE_LIMIT_EXCEEDED(429, "Rate Limit Exceeded");

    private final int errorCode;
    private final String errorMessage;

    ResponseStatus(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
} 