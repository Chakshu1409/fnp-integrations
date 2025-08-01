package com.fnp.integrations.lalamove.dtos;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LalamoveOrderRequestWrapper {

    private DataInner data;

    @Data
    public static class DataInner {
        private String quotationId;
        private Sender sender;
        private List<Recipient> recipients;
        private boolean isPODEnabled;
        private boolean isRecipientSMSEnabled;
        private String partner;
        private Metadata metadata;
    }

    @Data
    public static class Sender {
        private String stopId;
        private String name;
        private String phone;
    }

    @Data
    public static class Recipient {
        private String stopId;
        private String name;
        private String phone;
        private String remarks;
    }

    @Data
    public static class Metadata {
        private String MerchantId;
        private String restaurntName;
    }
}