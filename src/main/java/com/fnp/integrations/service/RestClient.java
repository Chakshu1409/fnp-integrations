package com.fnp.integrations.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;

/**
 * REST Client Interface
 * Provides centralized HTTP client operations with consistent error handling and logging
 */
public interface RestClient {

    // GET Methods
    <T> T get(String url, HttpHeaders httpHeaders, Class<T> returnType, boolean failFast, Object... uriVariables);
    
    <T> T get(String url, HttpHeaders httpHeaders, Class<T> returnType, boolean failFast, String token, Object... uriVariables);
    
    <T> T get(String url, HttpHeaders httpHeaders, ParameterizedTypeReference<T> returnType, boolean failFast, Object... uriVariables);
    
    <T> T get(String url, HttpHeaders httpHeaders, boolean failFast, Object... uriVariables);

    // POST Methods
    <T> T post(String url, HttpHeaders httpHeaders, Object payload, Class<T> returnType, boolean failFast, Object... uriVariables);
    
    <T> T post(String url, HttpHeaders httpHeaders, Object payload, ParameterizedTypeReference<T> returnType, boolean failFast, Object... uriVariables);
    
    <T> T post(String url, HttpHeaders httpHeaders, Object payload, boolean failFast, Object... uriVariables);

    // PUT Methods
    <T> T put(String url, HttpHeaders httpHeaders, Object payload, Class<T> returnType, boolean failFast, Object... uriVariables);
    
    <T> T put(String url, HttpHeaders httpHeaders, Object payload, ParameterizedTypeReference<T> returnType, boolean failFast, Object... uriVariables);
    
    <T> T put(String url, HttpHeaders httpHeaders, Object payload, boolean failFast, Object... uriVariables);

    // DELETE Methods
    <T> T delete(String url, HttpHeaders httpHeaders, Class<T> returnType, boolean failFast, Object... uriVariables);
    
    <T> T delete(String url, HttpHeaders httpHeaders, ParameterizedTypeReference<T> returnType, boolean failFast, Object... uriVariables);
    
    <T> T delete(String url, HttpHeaders httpHeaders, boolean failFast, Object... uriVariables);
} 