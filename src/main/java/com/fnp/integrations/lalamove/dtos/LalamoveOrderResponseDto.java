package com.fnp.integrations.lalamove.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LalamoveOrderResponseDto {
    private DataPayload data;

    @Data
    public static class DataPayload {
        private String orderId;
        private String quotationId;
        private PriceBreakdown priceBreakdown;
        private String driverId;
        private String shareLink;
        private String status;
        private Distance distance;
        private List<Stop> stops;
        private Metadata metadata;
        private String partner;
    }

    @Data
    public static class PriceBreakdown {
        private String base;
        private String extraMileage;
        private String totalExcludePriorityFee;
        private String total;
        private String currency;
    }

    @Data
    public static class Distance {
        private String value;
        private String unit;
    }

    @Data
    public static class Stop {
        private Coordinates coordinates;
        private String address;
        private String name;
        private String phone;

        @JsonProperty("delivery_code")
        private DeliveryCode deliveryCode;

        @JsonProperty("POD")
        private POD pod;
    }

    @Data
    public static class Coordinates {
        private String lat;
        private String lng;
    }

    @Data
    public static class DeliveryCode {
        private String value;
        private String status;
    }

    @Data
    public static class POD {
        private String status;
    }

    @Data
    public static class Metadata {
        @JsonProperty("MerchantId")
        private String MerchantId;

        @JsonProperty("restaurntName")
        private String restaurntName;
    }
}
