package com.fnp.integrations.lalamove.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LalamoveDeliveryRequestWrapper {
    private LalamoveDeliveryRequest data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LalamoveDeliveryRequest {
        private String scheduleAt;
        private String serviceType;
        private List<String> specialRequests;
        private String language;
        private List<Stop> stops;
        @JsonProperty("isRouteOptimized")
        private boolean isRouteOptimized; // Changed to routeOptimized to match Lalamove API
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stop {
        private Coordinates coordinates;
        private String address;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinates {
        private String lat;
        private String lng;
    }
}