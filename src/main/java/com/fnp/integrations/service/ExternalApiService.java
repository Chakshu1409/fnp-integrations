package com.fnp.integrations.service;

import com.fnp.integrations.constants.DynamicConstants;
import com.fnp.integrations.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiService {

    private final RestClient restClient;
    private final DynamicConstants constants;

    /**
     * Example: GET request to external API
     */
    public Map<String, Object> getExternalData(String endpoint) {
        try {
            String url = constants.getApiBaseUrl() + endpoint;
            HttpHeaders headers = createHeaders();
            
            log.info("Calling external API: {}", url);
            
            return restClient.get(url, headers, Map.class, true);
            
        } catch (Exception e) {
            log.error("Error calling external API: {}", e.getMessage());
            throw new ResponseException("Failed to fetch external data: " + e.getMessage());
        }
    }

    /**
     * Example: POST request to external API
     */
    public Map<String, Object> postExternalData(String endpoint, Object payload) {
        try {
            String url = constants.getApiBaseUrl() + endpoint;
            HttpHeaders headers = createHeaders();
            
            log.info("Posting data to external API: {}", url);
            
            return restClient.post(url, headers, payload, Map.class, true);
            
        } catch (Exception e) {
            log.error("Error posting to external API: {}", e.getMessage());
            throw new ResponseException("Failed to post external data: " + e.getMessage());
        }
    }

    /**
     * Example: GET request with ParameterizedTypeReference for complex responses
     */
    public Map<String, Object> getComplexResponse(String endpoint) {
        try {
            String url = constants.getApiBaseUrl() + endpoint;
            HttpHeaders headers = createHeaders();
            
            ParameterizedTypeReference<Map<String, Object>> responseType = 
                new ParameterizedTypeReference<Map<String, Object>>() {};
            
            return restClient.get(url, headers, responseType, true);
            
        } catch (Exception e) {
            log.error("Error calling external API: {}", e.getMessage());
            throw new ResponseException("Failed to fetch complex response: " + e.getMessage());
        }
    }

    /**
     * Example: PUT request to external API
     */
    public Map<String, Object> updateExternalData(String endpoint, Object payload) {
        try {
            String url = constants.getApiBaseUrl() + endpoint;
            HttpHeaders headers = createHeaders();
            
            log.info("Updating data at external API: {}", url);
            
            return restClient.put(url, headers, payload, Map.class, true);
            
        } catch (Exception e) {
            log.error("Error updating external API: {}", e.getMessage());
            throw new ResponseException("Failed to update external data: " + e.getMessage());
        }
    }

    /**
     * Example: DELETE request to external API
     */
    public void deleteExternalData(String endpoint) {
        try {
            String url = constants.getApiBaseUrl() + endpoint;
            HttpHeaders headers = createHeaders();
            
            log.info("Deleting data at external API: {}", url);
            
            restClient.delete(url, headers, Void.class, true);
            
        } catch (Exception e) {
            log.error("Error deleting from external API: {}", e.getMessage());
            throw new ResponseException("Failed to delete external data: " + e.getMessage());
        }
    }

    /**
     * Example: GET request with failFast=false (returns null instead of throwing exception)
     */
    public Map<String, Object> getExternalDataSafe(String endpoint) {
        try {
            String url = constants.getApiBaseUrl() + endpoint;
            HttpHeaders headers = createHeaders();
            
            log.info("Calling external API safely: {}", url);
            
            return restClient.get(url, headers, Map.class, false); // failFast=false
            
        } catch (Exception e) {
            log.warn("External API call failed, returning null: {}", e.getMessage());
            return null;
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "FNP-Integrations/1.0");
        headers.set("X-API-Key", "your-api-key"); // Add your API key here
        return headers;
    }
} 