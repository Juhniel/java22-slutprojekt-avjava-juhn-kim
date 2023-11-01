package org.juhnkim.controllers;

import org.juhnkim.interfaces.LogEventListenerInterface;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.Consumer;
import org.juhnkim.services.Producer;
import org.juhnkim.services.PropertyChangeService;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ThreadController {
    private final LinkedList<Producer> producerLinkedList;
    private final List<Consumer> consumerList;
    private final Buffer buffer;
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private final PropertyChangeService propertyChangeService;
    private boolean isAutoAdjustOn;
    private final javax.swing.Timer autoAdjustTimer;


    public ThreadController(ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer) {
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.buffer = buffer;
        this.propertyChangeService = new PropertyChangeService(productionRegulatorGUI, buffer);
        this.producerLinkedList = new LinkedList<>();
        this.consumerList = new ArrayList<>();
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

    public void initConsumers() {
        int numConsumers = (int) (Math.random() * 13) + 3;
        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer(buffer);
            consumerList.add(consumer);
            new Thread(consumer).start();
        }
        System.out.println("Consumers: " + consumerList.size());
    }

    public void addProducer() {
        Producer producer = new Producer(buffer);
        producerLinkedList.add(producer);
        new Thread(producer).start();
        Log.getInstance().logInfo("Producer Added");
        Log.getInstance().logInfo("Producer Count: " + producerLinkedList.size());
    }

    public void removeProducer() {
        if (!producerLinkedList.isEmpty()) {
            producerLinkedList.removeLast().stop();
            Log.getInstance().logInfo("Producer Removed");
            Log.getInstance().logInfo("Producer Count: " + producerLinkedList.size());
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
