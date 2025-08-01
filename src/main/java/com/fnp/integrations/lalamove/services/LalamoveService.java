package com.fnp.integrations.lalamove.services;

import com.fnp.integrations.lalamove.dtos.LalamoveOrderRequestWrapper;
import com.fnp.integrations.lalamove.dtos.LalamoveOrderResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnp.integrations.lalamove.dtos.LalamoveDeliveryRequestWrapper;
import com.fnp.integrations.lalamove.dtos.LalamoveQuotationResponseDto;

@Slf4j
@Service
public class LalamoveService {

    @Value("${lalamove.hostname}")
    private String hostname;
    
    @Value("${lalamove.appKey}")
    private String appKey;
    
    @Value("${lalamove.appSecret}")
    private String appSecret;
    
    @Value("${lalamove.market}")
    private String market;

    public LalamoveQuotationResponseDto getQuotations(LalamoveDeliveryRequestWrapper request) {
        
        // Force set isRouteOptimized to true since frontend sends true
        
        RestTemplate restTemplate = new RestTemplate();

        String method = "POST";
        String path = "/v3/quotations";
        long timestampMillis = System.currentTimeMillis();
        String timestamp = String.valueOf(timestampMillis);
        
        // Convert class to JSON via object mapper instead of toString
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(request);
        } catch (Exception e) {
            throw new RuntimeException("Error converting request to JSON", e);
        }
        String rawSignature = timestamp + "\r\n" + method + "\r\n" + path + "\r\n\r\n" + requestJson;

        String signature;
        try {
            signature = generateHmacSHA256(rawSignature, appSecret);
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature", e);
        }
        
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "hmac " + appKey + ":" + timestamp + ":" + signature);
        headers.set("market", market);

        
        HttpEntity<LalamoveDeliveryRequestWrapper> entity = new HttpEntity<>(request, headers);
        
        log.info("Complete Request : {}", entity);

        String url = "https://" + hostname + "/v3/quotations";
        log.info("Calling URL: {}", url);
        
        ResponseEntity<LalamoveQuotationResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, LalamoveQuotationResponseDto.class);
        
        log.info("Response status: {}", response.getStatusCode());
        log.info("Response body: {}", response.getBody());
        
        return response.getBody();
    }

    public LalamoveOrderResponseDto placeOrders(LalamoveOrderRequestWrapper request) {
        RestTemplate restTemplate = new RestTemplate();
        log.info("hi");
        String method = "POST";
        String path = "/v3/orders";
        long timestampMillis = System.currentTimeMillis();
        String timestamp = String.valueOf(timestampMillis);

        // Convert class to JSON via object mapper instead of toString
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(request);
            log.info("Request JSON: {}", requestJson);
        } catch (Exception e) {
            throw new RuntimeException("Error converting request to JSON", e);
        }
        log.info("Complete Request order: {}", requestJson);
        String rawSignature = timestamp + "\r\n" + method + "\r\n" + path + "\r\n\r\n" + requestJson;
        log.info("Raw signature: {}", rawSignature);

        String signature;
        try {
            signature = generateHmacSHA256(rawSignature, appSecret);
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature", e);
        }

        log.info("Generated signature: {}", signature);
        log.info("Authorization header order: hmac {}:{}:{}", appKey, timestamp, signature);

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "hmac " + appKey + ":" + timestamp + ":" + signature);
        headers.set("market", market);

        HttpEntity<LalamoveOrderRequestWrapper> entity = new HttpEntity<>(request, headers);

        String url = "https://" + hostname + "/v3/orders";

        ResponseEntity<LalamoveOrderResponseDto> response=restTemplate.exchange(url, HttpMethod.POST, entity, LalamoveOrderResponseDto.class);

        return response.getBody();
    }

    public static String generateHmacSHA256(String rawSignature, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(rawSignature.getBytes("UTF-8"));

        // Convert to hex string (to match CryptoJS's `.toString()`)
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
