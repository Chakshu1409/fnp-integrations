package com.fnp.integrations.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ResponseException extends RuntimeException {

    private Integer appErrorCode;
    private String errorMessage;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Default to 500
    private Map<String, Object> responseData;

    public ResponseException(Integer appErrorCode, String errorMessage) {
        super(errorMessage);
        this.appErrorCode = appErrorCode;
        this.errorMessage = errorMessage;
    }

    public ResponseException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.appErrorCode = 400;
    }

    public ResponseException(String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.appErrorCode = status.value();
        this.httpStatus = status;
    }

    public ResponseException(Integer appErrorCode, String errorMessage, Map<String, Object> responseData) {
        super(errorMessage);
        this.appErrorCode = appErrorCode;
        this.errorMessage = errorMessage;
        this.responseData = responseData;
    }

    public ResponseException(String errorMessage, Map<String, Object> responseData) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.appErrorCode = 400;
        this.responseData = responseData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getErrorCode() {
        return appErrorCode;
    }

    public Map<String, Object> getResponseData() {
        return responseData;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
} 