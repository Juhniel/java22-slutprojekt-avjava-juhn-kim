package org.juhnkim.controllers;

import org.juhnkim.models.Producer;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.ProducerService;
import org.juhnkim.services.PropertyChangeService;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import java.util.LinkedList;

public class ProducerController {

    private final Buffer buffer;
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private final PropertyChangeService propertyChangeService;
    private boolean isAutoAdjustOn;
    private final javax.swing.Timer autoAdjustTimer;
    private final LinkedList<Producer> producerList;
    private final LinkedList<ProducerService> producerServiceList;


    public ProducerController(ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer) {
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.buffer = buffer;
        this.producerList = new LinkedList<>();
        this.producerServiceList = new LinkedList<>();
        this.propertyChangeService = new PropertyChangeService(productionRegulatorGUI, buffer);
        this.isAutoAdjustOn = false;
        this.autoAdjustTimer = new javax.swing.Timer(4000, e -> autoAdjustProducers());
    }

    private void autoAdjustProducers() {
        double lowerThreshold = 35.0;
        double upperThreshold = 65.0;
        double balancePercentage = propertyChangeService.getBalancePercentage();
        if (balancePercentage < lowerThreshold) {
            addProducer();
            Log.getInstance().logInfo("Too few producers! Added a new producer.");
        } else if (balancePercentage > upperThreshold) {
            removeProducer();
            Log.getInstance().logInfo("Too many producers! Removed a producer.");
        }
    }

    public void addProducer() {
        Producer producer = new Producer();
        ProducerService producerService = new ProducerService(buffer, producer);
        producerList.add(producer);
        new Thread(producerService).start();
        Log.getInstance().logInfo("Producer Added");
        Log.getInstance().logInfo("Producer Count: " + producerList.size());
    }

    public void removeProducer() {
        if (!producerList.isEmpty()) {
            producerList.removeLast();
            ProducerService lastProducerService = producerServiceList.removeLast();
            lastProducerService.stop();

            Log.getInstance().logInfo("Producer Removed");
            Log.getInstance().logInfo("Producer Count: " + producerList.size());
        }
    }

    public void toggleAutoAdjust() {
        isAutoAdjustOn = !isAutoAdjustOn;
        productionRegulatorGUI.autoAdjustColor(isAutoAdjustOn);
        if (isAutoAdjustOn) {
            autoAdjustTimer.start();
        } else {
            autoAdjustTimer.stop();
        }
    }
}
