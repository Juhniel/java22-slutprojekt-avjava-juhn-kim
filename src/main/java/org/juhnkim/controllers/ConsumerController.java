package org.juhnkim.controllers;

import org.juhnkim.models.State;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.ConsumerService;

public class ConsumerController {
    private final ConsumerService consumerService;

    public ConsumerController(Buffer buffer, State state) {
        this.consumerService = new ConsumerService(buffer, state);
    }

    public void initialize() {
        consumerService.initConsumers();
    }
}
