package org.juhnkim.controllers;

import org.juhnkim.services.ProducerService;

public class ProducerController {
    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    public void addProducer() {
        producerService.addProducer();
    }

    public void removeProducer() {
        producerService.removeProducer();
    }

    public void toggleAutoAdjust() {
        producerService.toggleAutoAdjust();
    }
}
