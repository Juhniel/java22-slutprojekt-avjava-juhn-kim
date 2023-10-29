package org.juhnkim.controllers;

import org.apache.logging.log4j.Logger;
import org.juhnkim.models.Message;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.Consumer;
import org.juhnkim.services.Producer;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

public class Controller implements PropertyChangeListener {
    private final Logger logger = Log.getInstance().getLogger();
    private final LinkedList<Producer> producerLinkedList;
    private final LinkedList<Consumer> consumersLinkedList;
    private final Buffer buffer;
    private final Message message;
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private boolean isAutoAdjustOn = false;

    /**
     * Calculate the amount of messages in queue with a capacity of 100
     */
    double balancePercentage;
    private Producer producer;

    public Controller(Message message, ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer) {
        this.message = message;
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.buffer = buffer;
        buffer.addPropertyChangeListener(this);
        this.producerLinkedList = new LinkedList<>();
        this.consumersLinkedList = new LinkedList<>();
        initController();
        initConsumers();
        new javax.swing.Timer(0, e -> updateProgressBar()).start();
    }

    private void initController() {
        productionRegulatorGUI.getAddButton().addActionListener(e -> addProducer());
        productionRegulatorGUI.getRemoveButton().addActionListener(e -> removeProducer());
        productionRegulatorGUI.getSaveButton().addActionListener(e -> saveCurrentState());
        productionRegulatorGUI.getLoadButton().addActionListener(e -> loadSavedState());
        productionRegulatorGUI.getAutoAdjustButton().addActionListener(e -> toggleAutoAdjust());
    }

    private void toggleAutoAdjust() {
        isAutoAdjustOn = !isAutoAdjustOn;
        productionRegulatorGUI.autoAdjustColor(isAutoAdjustOn);
        adjustProducers();
    }

    /**
     * Initialize 3-15 consumer threads when the application starts
     */
    private void initConsumers() {
        int numConsumers = (int) (Math.random() * 13) + 3;
        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer(buffer);
            consumersLinkedList.add(consumer);
            new Thread(consumer).start();
        }
        System.out.println("Consumers: " + consumersLinkedList.size());
    }

    /**
     * Adding a new Producer Thread
     */
    private void addProducer() {
        producer = new Producer(buffer);
        producerLinkedList.add(producer);
        new Thread(producer).start();
        logger.info("Producer Added");
        logger.info("New producer count: " + producerLinkedList.size());
    }

    /**
     * Removing a Producer Thread
     */
    private void removeProducer() {
        if (!producerLinkedList.isEmpty()) {
            producerLinkedList.removeLast().stop();
            logger.info("Producer Removed");
            logger.info("New producer count: " + producerLinkedList.size());
        }
    }

    private void saveCurrentState() {
        // Save current state
    }

    private void loadSavedState() {
        // Load the state
    }

    private void updateProgressBar() {
        balancePercentage = ((double) buffer.getMessageCount() / buffer.getCapacity() * 100);
        productionRegulatorGUI.updateProgressBar(balancePercentage);
    }

    private void averageMessages() {
        // Summera antal meddelanden för varje sekund
        // dela med 10 sekunder för att få genomsnittet hur många meddelanden som har producerats

//        double calculatedAverage = buffer.getMessageCount()
//        logger.info("Average value of units: " + calculatedAverage);
    }

    long lastProducerAdjustmentTime = 0;
    long producerAdjustmentInterval = 20000; // 10 seconds
    double lowerThreshold = 45.0;
    double upperThreshold = 55.0;

    void adjustProducers() {
        while(isAutoAdjustOn) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastProducerAdjustmentTime < producerAdjustmentInterval) {
                return;
            }

            if (balancePercentage < lowerThreshold) {
                addProducer();
                logger.info("Too few producers! Added a new producer.");
            } else if (balancePercentage > upperThreshold) {
                removeProducer();
                logger.info("Too many producers! Removed a producer.");
            }

            lastProducerAdjustmentTime = currentTime;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}


