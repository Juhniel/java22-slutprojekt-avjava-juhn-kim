package org.juhnkim.controllers;

import org.apache.logging.log4j.Logger;
import org.juhnkim.interfaces.LogEventListener;
import org.juhnkim.models.Message;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.Consumer;
import org.juhnkim.services.Producer;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Controller implements PropertyChangeListener, LogEventListener {
    private final LinkedList<Producer> producerLinkedList;
    private final LinkedList<Consumer> consumersLinkedList;
    private final List<Integer> messageCounts = new ArrayList<>();
    private final Buffer buffer;
    private final Message message;
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private boolean isAutoAdjustOn = false;

    String logMessage = "";
    double balancePercentage;
    private Producer producer;

    public Controller(Message message, ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer) {
        this.message = message;
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.buffer = buffer;
        buffer.addPropertyChangeListener(this);
        Log.getInstance().addLogEventListener(this);
        this.producerLinkedList = new LinkedList<>();
        this.consumersLinkedList = new LinkedList<>();
        initController();
        initConsumers();
        new javax.swing.Timer(0, e -> updateProgressBar()).start();
        new javax.swing.Timer(1000, e -> averageMessages()).start();
        new javax.swing.Timer(10000, e -> logAverageMessages()).start();
    }

    /**
     * Initialize buttons on the gui /w ActionListeners
     */
    private void initController() {
        productionRegulatorGUI.getAddButton().addActionListener(e -> addProducer());
        productionRegulatorGUI.getRemoveButton().addActionListener(e -> removeProducer());
        productionRegulatorGUI.getSaveButton().addActionListener(e -> saveCurrentState());
        productionRegulatorGUI.getLoadButton().addActionListener(e -> loadSavedState());
        productionRegulatorGUI.getAutoAdjustButton().addActionListener(e -> toggleAutoAdjust());
    }

    /**
     * Method for toggling on and off for auto-adjust
     */
    private void toggleAutoAdjust() {
        isAutoAdjustOn = !isAutoAdjustOn;
        productionRegulatorGUI.autoAdjustColor(isAutoAdjustOn);
        autoAdjustProducers();
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
        Log.getInstance().logInfo("Producer Added");
        Log.getInstance().logInfo("New producer count: " + producerLinkedList.size());
    }

    /**
     * Removing a Producer Thread
     */
    private void removeProducer() {
        if (!producerLinkedList.isEmpty()) {
            producerLinkedList.removeLast().stop();
            Log.getInstance().logInfo("Producer Removed");
            Log.getInstance().logInfo("New producer count: " + producerLinkedList.size());
        }
    }

    private void saveCurrentState() {
        // Save current state
        // stream I/O

    }

    private void loadSavedState() {
        // Load the state
        // stream I/O
    }

    private void updateProgressBar() {
        balancePercentage = ((double) buffer.getMessageCount() / buffer.getCapacity() * 100);
        productionRegulatorGUI.updateProgressBar(balancePercentage);
    }

    private void logProducerWarnings() {
        if(balancePercentage <= 10) {
            Log.getInstance().logInfo("Amount of producer too low!");
        } else if(balancePercentage >= 90) {
            Log.getInstance().logInfo("Amount of producers are too high!");
        }
    }

    private void averageMessages() {
        int currentMessageCount = buffer.getMessageCount();
        messageCounts.add(currentMessageCount);
    }

    private void logAverageMessages() {
        if (messageCounts.isEmpty()) {
            Log.getInstance().logInfo("No data for average");
            return;
        }
        double average = messageCounts.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        Log.getInstance().logInfo("Average number of messages in the buffer over the last 10 seconds: " + average);
        messageCounts.clear();

        // log if there are too many or too few Producers every 10 seconds
        logProducerWarnings();
    }



    /**
     *  Method for auto-adjusting the Producers to try and balance every 2 seconds.
     */
    long lastProducerAdjustmentTime = 0;
    long producerAdjustmentInterval = 20000;
    double lowerThreshold = 45.0;
    double upperThreshold = 55.0;

    void autoAdjustProducers() {
        while(isAutoAdjustOn) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastProducerAdjustmentTime < producerAdjustmentInterval) {
                return;
            }

            if (balancePercentage < lowerThreshold) {
                addProducer();
                Log.getInstance().logInfo("Too few producers! Added a new producer.");
            } else if (balancePercentage > upperThreshold) {
                removeProducer();
                Log.getInstance().logInfo("Too many producers! Removed a producer.");
            }

            lastProducerAdjustmentTime = currentTime;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void onLogEvent(String message) {
        String existingText = productionRegulatorGUI.getTextArea().getText();
        productionRegulatorGUI.getTextArea().setText(message + "\n" + existingText);
    }
}


