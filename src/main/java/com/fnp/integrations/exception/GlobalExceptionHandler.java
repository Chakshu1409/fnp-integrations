package com.fnp.integrations.exception;

import com.fnp.integrations.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ResponseDto<Map<String, Object>>> handleResponseException(ResponseException ex, WebRequest request) {
        log.error("ResponseException occurred: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("timestamp", LocalDateTime.now());
        errorData.put("path", request.getDescription(false));
        
        if (ex.getResponseData() != null) {
            errorData.putAll(ex.getResponseData());
        }
        
        ResponseDto<Map<String, Object>> responseDto = ResponseDto.error(
                ex.getHttpStatus().value(),
                ex.getErrorCode(),
                ex.getErrorMessage(),
                errorData
        );
        
        return new ResponseEntity<>(responseDto, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Map<String, Object>>> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("timestamp", LocalDateTime.now());
        errorData.put("path", request.getDescription(false));
        
        ResponseDto<Map<String, Object>> responseDto = ResponseDto.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                500,
                "Internal Server Error",
                errorData
        );
        
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto<Map<String, Object>>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("IllegalArgumentException occurred: {}", ex.getMessage(), ex);
        
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("timestamp", LocalDateTime.now());
        errorData.put("path", request.getDescription(false));
        
        ResponseDto<Map<String, Object>> responseDto = ResponseDto.error(
                HttpStatus.BAD_REQUEST.value(),
                400,
                ex.getMessage(),
                errorData
        );
        
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
} 