package com.fnp.integrations.lalamove.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LalamoveQuotationResponseDto {
    
    private QuotationData data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuotationData {
        private String quotationId;
        private String scheduleAt;
        private String expiresAt;
        private String serviceType;
        private String language;
        private List<Stop> stops;
        private boolean isRouteOptimized;
        private PriceBreakdown priceBreakdown;
        private Distance distance;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stop {
        private String stopId;
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
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceBreakdown {
        private String base;
        private String extraMileage;
        private String surcharge;
        private String totalBeforeOptimization;
        private String totalExcludePriorityFee;
        private String total;
        private String currency;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Distance {
        private String value;
        private String unit;
    }
} 