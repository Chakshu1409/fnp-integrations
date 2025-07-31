package com.fnp.integrations.service.impl;

import com.fnp.integrations.enums.ResponseStatus;
import com.fnp.integrations.exception.ResponseException;
import com.fnp.integrations.service.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Collections;

/**
 * FNP Integrations REST Client
 * The client is built on RestTemplate and provides:
 * - Authorization handling with retry for 401 errors
 * - Comprehensive logging and profiling
 * - Internal and client-level error handling with fast fail option
 * - Consistent error responses using ResponseException
 */
@Component
@Slf4j
public class RestClientImpl implements RestClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final boolean isLogsDisabled = false; // Enable logs for debugging
    
    @Autowired
    private RestTemplate restTemplate;

    private String getValueAsString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception ex) {
            return null;
        }
    }

    private String getHost(String url) {
        try {
            URL aURL = new URL(url);
            return aURL.getHost();
        } catch (Exception ex) {
            return null;
        }
    }

    // GET Methods Implementation
    @Override
    public <T> T get(String url, HttpHeaders httpHeaders, Class<T> returnType, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.GET, httpHeaders, null, returnType, null, failFast, true, isLogsDisabled, null, uriVariables);
    }

    @Override
    public <T> T get(String url, HttpHeaders httpHeaders, Class<T> returnType, boolean failFast, String token, Object... uriVariables) {
        return this.any(url, HttpMethod.GET, httpHeaders, null, returnType, null, failFast, false, isLogsDisabled, token, uriVariables);
    }

    @Override
    public <T> T get(String url, HttpHeaders httpHeaders, ParameterizedTypeReference<T> returnType, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.GET, httpHeaders, null, null, returnType, failFast, true, isLogsDisabled, null, uriVariables);
    }

    @Override
    public <T> T get(String url, HttpHeaders httpHeaders, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.GET, httpHeaders, null, null, null, failFast, true, isLogsDisabled, null, uriVariables);
    }

    // POST Methods Implementation
    @Override
    public <T> T post(String url, HttpHeaders httpHeaders, Object payload, Class<T> returnType, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.POST, httpHeaders, payload, returnType, null, failFast, true, isLogsDisabled, null, uriVariables);
    }

    @Override
    public <T> T post(String url, HttpHeaders httpHeaders, Object payload, ParameterizedTypeReference<T> returnType, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.POST, httpHeaders, payload, null, returnType, failFast, true, isLogsDisabled, null, uriVariables);
    }

    @Override
    public <T> T post(String url, HttpHeaders httpHeaders, Object payload, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.POST, httpHeaders, payload, null, null, failFast, true, isLogsDisabled, null, uriVariables);
    }

    // PUT Methods Implementation
    @Override
    public <T> T put(String url, HttpHeaders httpHeaders, Object payload, Class<T> returnType, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.PUT, httpHeaders, payload, returnType, null, failFast, true, isLogsDisabled, null, uriVariables);
    }

    @Override
    public <T> T put(String url, HttpHeaders httpHeaders, Object payload, ParameterizedTypeReference<T> returnType, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.PUT, httpHeaders, payload, null, returnType, failFast, true, isLogsDisabled, null, uriVariables);
    }

    @Override
    public <T> T put(String url, HttpHeaders httpHeaders, Object payload, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.PUT, httpHeaders, payload, null, null, failFast, true, isLogsDisabled, null, uriVariables);
    }

    // DELETE Methods Implementation
    @Override
    public <T> T delete(String url, HttpHeaders httpHeaders, Class<T> returnType, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.DELETE, httpHeaders, null, returnType, null, failFast, true, isLogsDisabled, null, uriVariables);
    }

    @Override
    public <T> T delete(String url, HttpHeaders httpHeaders, ParameterizedTypeReference<T> returnType, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.DELETE, httpHeaders, null, null, returnType, failFast, true, isLogsDisabled, null, uriVariables);
    }

    @Override
    public <T> T delete(String url, HttpHeaders httpHeaders, boolean failFast, Object... uriVariables) {
        return this.any(url, HttpMethod.DELETE, httpHeaders, null, null, null, failFast, true, isLogsDisabled, null, uriVariables);
    }

    // Core HTTP method implementation
    private <T> T any(String url, HttpMethod httpMethod, HttpHeaders httpHeaders, Object payload,
                     Class<T> responseTypeClass, ParameterizedTypeReference<T> responseTypeRef,
                     boolean failFast, boolean retry, boolean disableResponseLogs,
                     String token, Object... uriVariables) {

        log.debug("Method: {}, URL: {}, HTTP Method: {}, URI Variables: {}, Payload: {}",
                Thread.currentThread().getStackTrace()[retry ? 4 : 3].getMethodName(), url, httpMethod, uriVariables,
                getValueAsString(payload));
        log.debug("Headers: {}, Fail Fast: {}", httpHeaders, failFast);

        HttpHeaders customHttpHeaders = new HttpHeaders();
        if (!CollectionUtils.isEmpty(httpHeaders)) {
            customHttpHeaders.putAll(httpHeaders);
        }

        if (null == customHttpHeaders.getContentType()) {
            customHttpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        customHttpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            
            ResponseEntity<T> response = null != responseTypeRef ? 
                    restTemplate.exchange(url, httpMethod, new HttpEntity<>(payload, customHttpHeaders), responseTypeRef, uriVariables) :
                    restTemplate.exchange(url, httpMethod, new HttpEntity<>(payload, customHttpHeaders), responseTypeClass, uriVariables);
            
            stopWatch.stop();
            log.debug("Time elapsed: {}ms", stopWatch.getTotalTimeMillis());

            if (disableResponseLogs) {
                log.debug("Response received: {}", null != response ? getValueAsString(response.getBody()) : null);
            } else {
                log.info("Response received: {}", null != response ? getValueAsString(response.getBody()) : null);
            }

            return null != response ? response.getBody() : null;
            
        } catch (HttpClientErrorException ex) {
            if (retry && (null != ex.getMessage() && ex.getMessage().toLowerCase().contains(HttpStatus.UNAUTHORIZED.getReasonPhrase().toLowerCase())
                    || ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED))) {
                return this.any(url, httpMethod, httpHeaders, payload, responseTypeClass,
                        responseTypeRef, failFast, false, disableResponseLogs, token, uriVariables);
            } else {
                return handleClientError(failFast, url, ex);
            }
        } catch (Throwable e) {
            return handleInternalError(failFast, url, e);
        }
    }

    private <T> T handleInternalError(boolean failFast, String url, Throwable ex) {
        log.error("Exception occurred while executing HTTP request. Error: {}", ex.getMessage(), ex);
        String host = getHost(url);
        
        if (failFast) {
            throw new ResponseException(
                    ResponseStatus.MICROSERVICE_INTERNAL_ERROR.getErrorCode(),
                    ResponseStatus.MICROSERVICE_INTERNAL_ERROR.getErrorMessage() + (null != host ? " HOST: " + host : "")
            );
        }
        return null;
    }

    private <T> T handleClientError(boolean failFast, String url, HttpClientErrorException ex) {
        log.error("HTTP client error. Status: {}, Message: {}", ex.getStatusCode(), ex.getMessage());
        String host = getHost(url);

        if (!failFast) {
            return null;
        }

        if (ex.getStatusCode() != null) {
            switch (ex.getStatusCode().value()) {
                case 400:
                    throw new ResponseException(
                            ResponseStatus.BAD_REQUEST.getErrorCode(),
                            (null != ex.getResponseBodyAsString() ? ex.getResponseBodyAsString()
                                    : ResponseStatus.BAD_REQUEST.getErrorMessage())
                                    + (null != host ? " HOST: " + host : "")
                    );
                case 401:
                    throw new ResponseException(
                            ResponseStatus.MICROSERVICE_AUTHORIZATION_ERROR.getErrorCode(),
                            (null != ex.getResponseBodyAsString() ? ex.getResponseBodyAsString()
                                    : ResponseStatus.MICROSERVICE_AUTHORIZATION_ERROR.getErrorMessage())
                                    + (null != host ? " HOST: " + host : "")
                    );
                case 404:
                    throw new ResponseException(
                            ResponseStatus.MICROSERVICE_RESOURCE_NOT_FOUND.getErrorCode(),
                            (null != ex.getResponseBodyAsString() ? ex.getResponseBodyAsString()
                                    : ResponseStatus.MICROSERVICE_RESOURCE_NOT_FOUND.getErrorMessage())
                                    + (null != host ? " HOST: " + host : "")
                    );
                case 429:
                    throw new ResponseException(
                            ResponseStatus.RATE_LIMIT_EXCEEDED.getErrorCode(),
                            (null != ex.getResponseBodyAsString() ? ex.getResponseBodyAsString()
                                    : ResponseStatus.RATE_LIMIT_EXCEEDED.getErrorMessage())
                                    + (null != host ? " HOST: " + host : "")
                    );
                default:
                    throw new ResponseException(
                            ResponseStatus.MICROSERVICE_EXCHANGE_ERROR.getErrorCode(),
                            (null != ex.getResponseBodyAsString() ? ex.getResponseBodyAsString()
                                    : ResponseStatus.MICROSERVICE_EXCHANGE_ERROR.getErrorMessage())
                                    + (null != host ? " HOST: " + host : "")
                    );
            }
        } else {
            throw new ResponseException(
                    ResponseStatus.MICROSERVICE_EXCHANGE_ERROR.getErrorCode(),
                    ResponseStatus.MICROSERVICE_EXCHANGE_ERROR.getErrorMessage() + (null != host ? " HOST: " + host : "")
            );
        }
    }
} 