package org.juhnkim.controllers;

import org.juhnkim.services.ProducerService;
import org.juhnkim.views.ProductionRegulatorGUI;

public class ProducerController {
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private final ProducerService producerService;

    public ProducerController(ProductionRegulatorGUI productionRegulatorGUI, ProducerService producerService) {
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.producerService = producerService;
    }

    public void autoAdjustProducers() {
        producerService.autoAdjustProducers();
    }

    public void addProducer() {
        producerService.addProducer();
        updateProducerCountDisplay();
    }

    public void removeProducer() {
        producerService.removeProducer();
        updateProducerCountDisplay();
    }

    public void toggleAutoAdjust() {
        productionRegulatorGUI.autoAdjustColor();
    }

    private void updateProducerCountDisplay() {
        int producerCount = producerService.getProducerCount();
        // Update the UI component with the new count
        productionRegulatorGUI.setProducerCount(producerCount);
    }
}
