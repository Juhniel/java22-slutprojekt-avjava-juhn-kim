package org.juhnkim.controllers;

import org.juhnkim.interfaces.LogEventListenerInterface;
import org.juhnkim.interfaces.ProductionRegulatorInterface;
import org.juhnkim.models.Message;
import org.juhnkim.models.State;
import org.juhnkim.services.StateService;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.Consumer;
import org.juhnkim.services.Producer;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Controller implements ProductionRegulatorInterface, PropertyChangeListener, LogEventListenerInterface {
    private final LinkedList<Producer> producerLinkedList;
    private LinkedList<Integer> producerIntervals;
    private final List<Consumer> consumerList;
    private List<Integer> consumerIntervals;
    private List<Message> allMessagesInBuffer;
    private final Buffer buffer;
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private boolean isAutoAdjustOn = false;
    private double balancePercentage;
    private final javax.swing.Timer autoAdjustTimer;


    public Controller(ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer) {
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.buffer = buffer;
        buffer.addPropertyChangeListener(this);
        Log.getInstance().addLogEventListener(this);
        this.producerLinkedList = new LinkedList<>();
        this.producerIntervals = new LinkedList<>();
        this.consumerList = new ArrayList<>();
        this.consumerIntervals = new LinkedList<>();
        this.allMessagesInBuffer = new ArrayList<>();
        initController();
        initConsumers();
        new javax.swing.Timer(10000, e -> logConsumedToProducerRatio()).start();
        autoAdjustTimer = new javax.swing.Timer(4000, e -> autoAdjustProducers());
    }

    /**
     * Initialize buttons on the gui and set ActionListeners to buttons
     */
    private void initController() {
        productionRegulatorGUI.getAddButton().addActionListener(e -> addProducer());
        productionRegulatorGUI.getRemoveButton().addActionListener(e -> removeProducer());
        productionRegulatorGUI.getSaveButton().addActionListener(e -> saveCurrentState());
        productionRegulatorGUI.getLoadButton().addActionListener(e -> loadSavedState());
        productionRegulatorGUI.getAutoAdjustButton().addActionListener(e -> toggleAutoAdjust());
    }


    /**
     * Logs the average message count in the buffer every 10 seconds.
     */
    private void logConsumedToProducerRatio() {
        if (buffer.getProducedMessages() == 0) {
            Log.getInstance().logInfo("No data for average");
            return;
        }

        double consumedRatio = ((double) buffer.getConsumedMessages() / buffer.getProducedMessages()) * 100;
        System.out.println("Produced Messages: " + buffer.getProducedMessages());
        System.out.println("Consumed Messages: " + buffer.getConsumedMessages());

        Log.getInstance().logInfo("Average Consumed Messages: " + String.format("%.2f", consumedRatio) + "%");
        buffer.setProducedMessages(0);
        buffer.setConsumedMessages(0);

        // log if there are too many or too few Producers every 10 seconds
        logProducerWarnings();
    }

    /**
     * Updates the progress bar on the GUI based on buffer fill level.
     */
    private void updateProgressBar() {
        balancePercentage = ((double) buffer.getMessageCount() / buffer.getCapacity() * 100);
        productionRegulatorGUI.updateProgressBar(balancePercentage);
    }

    /**
     * Method for toggling on and off for auto-adjust
     */
    @Override
    public void toggleAutoAdjust() {
        isAutoAdjustOn = !isAutoAdjustOn;
        productionRegulatorGUI.autoAdjustColor(isAutoAdjustOn);
        if (isAutoAdjustOn) {
            autoAdjustTimer.start();
        } else {
            autoAdjustTimer.stop();
        }
    }

    /**
     * Initialize 3-15 consumer threads when the application starts
     */
    private void initConsumers() {
        int numConsumers = (int) (Math.random() * 13) + 3;
        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer(buffer);
            consumerList.add(consumer);
            new Thread(consumer).start();
        }
        System.out.println("Consumers: " + consumerList.size());
    }

    /**
     * Adding a new Producer Thread
     */
    @Override
    public void addProducer() {
        Producer producer = new Producer(buffer);
        producerLinkedList.add(producer);
        new Thread(producer).start();
        Log.getInstance().logInfo("Producer Added");
        Log.getInstance().logInfo("New producer count: " + producerLinkedList.size());
    }

    /**
     * Removing a Producer Thread
     */
    @Override
    public void removeProducer() {
        if (!producerLinkedList.isEmpty()) {
            producerLinkedList.removeLast().stop();

            Log.getInstance().logInfo("Producer Removed");
            Log.getInstance().logInfo("New producer count: " + producerLinkedList.size());
        }
    }

    /**
     * Saves the current state of the application.
     */
    @Override
    public void saveCurrentState() {
        for (Producer p : producerLinkedList) {
            producerIntervals.add(p.getProducerInterval());
        }


        for (Consumer c : consumerList) {
            consumerIntervals.add(c.getConsumerInterval());
        }

        allMessagesInBuffer = buffer.getAllMessagesInBuffer();

        State state = new State(producerLinkedList, consumerList, producerIntervals, consumerIntervals, allMessagesInBuffer);
        StateService stateService = new StateService();
        stateService.saveState(state);
    }

    /**
     * Loads a saved state of the application.
     */
    @Override
    public void loadSavedState() {
        StateService stateService = new StateService();
        State state = stateService.loadState();

        if (state != null) {
            // Clear current Producers and Consumers
            producerLinkedList.clear();
            consumerList.clear();
            buffer.clear();

            // Load Producers
            producerIntervals = state.getProducerIntervals();
            for (Integer producerInterval : producerIntervals) {
                Producer producer = new Producer(buffer);
                producer.setProducerInterval(producerInterval);
                producerLinkedList.add(producer);
                new Thread(producer).start();
            }

            // Load Consumers
            consumerIntervals = state.getConsumerIntervals();
            for (Integer consumerInterval : consumerIntervals) {
                Consumer consumer = new Consumer(buffer);
                consumer.setConsumerInterval(consumerInterval);
                consumerList.add(consumer);
                new Thread(consumer).start();
            }

            // Load Messages
            allMessagesInBuffer = state.getMessageList();
            buffer.setAllMessagesInBuffer(allMessagesInBuffer);

            Log.getInstance().logInfo("State loaded successfully.");
            Log.getInstance().logInfo("Amount of Producers loaded: " + state.getProducerList().size());
            Log.getInstance().logInfo("Amount of Consumers loaded: " + state.getConsumerList().size());
            Log.getInstance().logInfo("Amount of Messages in queue loaded: " + state.getMessageList().size());
        } else {
            Log.getInstance().logInfo("Failed to load state.");
        }
    }

    /**
     * Logs warnings related to the number of Producer threads.
     */
    @Override
    public void logProducerWarnings() {
        if (balancePercentage <= 10) {
            Log.getInstance().logInfo("Amount of producer too low!");
        } else if (balancePercentage >= 90) {
            Log.getInstance().logInfo("Amount of producers are too high!");
        }
    }

    /**
     * Auto-adjusts the number of Producers to balance the buffer fill level.
     */
    @Override
    public void autoAdjustProducers() {
        double lowerThreshold = 35.0;
        double upperThreshold = 65.0;

        if (balancePercentage < lowerThreshold) {
            addProducer();
            Log.getInstance().logInfo("Too few producers! Added a new producer.");
        } else if (balancePercentage > upperThreshold) {
            removeProducer();
            Log.getInstance().logInfo("Too many producers! Removed a producer.");
        }
    }

    /**
     * Handles log events to display them on the GUI.
     *
     * @param message Log message
     *                Implements LogEventListerInterface
     */
    @Override
    public void onLogEvent(String message) {
        SwingUtilities.invokeLater(() -> {
            String existingText = productionRegulatorGUI.getTextArea().getText();
            productionRegulatorGUI.getTextArea().setText(message + "\n" + existingText);
            productionRegulatorGUI.getTextArea().setCaretPosition(0);
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("messageCount".equals(evt.getPropertyName())) {
            updateProgressBar();
        }
    }
}


