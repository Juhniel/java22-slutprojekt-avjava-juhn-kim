package org.juhnkim.controllers;

import org.juhnkim.services.ConsumerService;

public class ConsumerController {
    private final ConsumerService consumerService;

    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    public void initialize() {
        consumerService.initConsumers();
    }
}
