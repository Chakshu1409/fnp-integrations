package com.fnp.integrations.lalamove.controllers;

import com.fnp.integrations.lalamove.dtos.LalamoveOrderRequestWrapper;
import com.fnp.integrations.lalamove.dtos.LalamoveOrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fnp.integrations.lalamove.dtos.LalamoveDeliveryRequestWrapper;
import com.fnp.integrations.lalamove.dtos.LalamoveQuotationResponseDto;
import com.fnp.integrations.lalamove.services.LalamoveService;

@RestController
@RequestMapping("/api/lalamove")
public class LalamoveController {

    @Autowired
    private LalamoveService lalamoveService;

    @PostMapping("/quotations")
    public ResponseEntity<LalamoveQuotationResponseDto> getQuotations(@RequestBody LalamoveDeliveryRequestWrapper request) {
        // Call the service to get quotations from Lalamove API
        return ResponseEntity.ok(lalamoveService.getQuotations(request));
    }

    @PostMapping("/orders")
    public ResponseEntity<LalamoveOrderResponseDto> placeOrders(@RequestBody LalamoveOrderRequestWrapper request) {
        return ResponseEntity.ok(lalamoveService.placeOrders(request));
    }
}