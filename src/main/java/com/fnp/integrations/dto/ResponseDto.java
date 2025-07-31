package com.fnp.integrations.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ResponseDto<T> {
    private String status;
    private Integer statusCode;
    private Integer errorCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T response;
    private Map<String, Object> responseData;

    public ResponseDto(String status, Integer statusCode, String message, T response) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.response = response;
    }

    public ResponseDto(String status, Integer statusCode, Integer errorCode, String message, T response) {
        this.status = status;
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
        this.response = response;
    }

    public ResponseDto(String status, Integer statusCode, String message, T response, Map<String, Object> responseData) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.response = response;
        this.responseData = responseData;
    }

    // Static factory methods for common responses
    public static <T> ResponseDto<T> success(T response) {
        return new ResponseDto<>("SUCCESS", 200, "Operation completed successfully", response);
    }

    public static <T> ResponseDto<T> success(String message, T response) {
        return new ResponseDto<>("SUCCESS", 200, message, response);
    }

    public static <T> ResponseDto<T> success(String message, T response, Map<String, Object> responseData) {
        return new ResponseDto<>("SUCCESS", 200, message, response, responseData);
    }

    public static <T> ResponseDto<T> error(Integer errorCode, String message) {
        return new ResponseDto<>("ERROR", 400, errorCode, message, null);
    }

    public static <T> ResponseDto<T> error(Integer statusCode, Integer errorCode, String message) {
        return new ResponseDto<>("ERROR", statusCode, errorCode, message, null);
    }

    public static <T> ResponseDto<T> error(Integer statusCode, Integer errorCode, String message, T response) {
        return new ResponseDto<>("ERROR", statusCode, errorCode, message, response);
    }
} 