package org.juhnkim.controllers;

import org.juhnkim.interfaces.LogEventListenerInterface;
import org.juhnkim.interfaces.ProductionRegulatorInterface;
import org.juhnkim.models.State;
import org.juhnkim.services.StateService;
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

public class Controller implements ProductionRegulatorInterface,PropertyChangeListener, LogEventListenerInterface {
    private final LinkedList<Producer> producerLinkedList;
    private final LinkedList<Consumer> consumersLinkedList;
    private final List<Integer> messageCounts = new ArrayList<>();
    private final Buffer buffer;
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private boolean isAutoAdjustOn = false;
    private double balancePercentage;


    public Controller(ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer) {
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
        new javax.swing.Timer(4000, e-> autoAdjustProducers()).start();
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
     * Computes the average message count in the buffer every second and adds to a List
     */
    private void averageMessages() {
        int currentMessageCount = buffer.getMessageCount();
        messageCounts.add(currentMessageCount);
    }

    /**
     * Logs the average message count in the buffer every 10 seconds.
     */
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
        List<Integer> producerIntervals = new ArrayList<>();
        for (Producer p : producerLinkedList) {
            producerIntervals.add(p.getProducerInterval());
        }

        List<Integer> consumerIntervals = new ArrayList<>();
        for (Consumer c : consumersLinkedList) {
            consumerIntervals.add(c.getConsumerInterval());
        }

        State state = new State(producerLinkedList, consumersLinkedList, producerIntervals, consumerIntervals);
        StateService stateService = new StateService();
        stateService.saveState(state);

        System.out.println(state);
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
            consumersLinkedList.clear();
            buffer.clear();

            // Load Producers
            List<Integer> producerIntervals = state.getProducerIntervals();
            for (int i = 0; i < producerIntervals.size(); i++) {
                Producer producer = new Producer(buffer);
                producer.setProducerInterval(producerIntervals.get(i));
                producerLinkedList.add(producer);
                new Thread(producer).start();
            }

            // Load Consumers
            List<Integer> consumerIntervals = state.getConsumerIntervals();
            for (int i = 0; i < consumerIntervals.size(); i++) {
                Consumer consumer = new Consumer(buffer);
                consumer.setConsumerInterval(consumerIntervals.get(i));
                consumersLinkedList.add(consumer);
                new Thread(consumer).start();
            }

            Log.getInstance().logInfo("State loaded successfully.");
        } else {
            Log.getInstance().logInfo("Failed to load state.");
        }
    }

    /**
     * Logs warnings related to the number of Producer threads.
     */
    @Override
    public void logProducerWarnings() {
        if(balancePercentage <= 10) {
            Log.getInstance().logInfo("Amount of producer too low!");
        } else if(balancePercentage >= 90) {
            Log.getInstance().logInfo("Amount of producers are too high!");
        }
    }

    /**
     * Auto-adjusts the number of Producers to balance the buffer fill level.
     */

    double lowerThreshold = 35.0;
    double upperThreshold = 75.0;
    @Override
    public void autoAdjustProducers() {
            if (balancePercentage < lowerThreshold) {
                addProducer();
                Log.getInstance().logInfo("Too few producers! Added a new producer.");
            } else if (balancePercentage > upperThreshold) {
                removeProducer();
                Log.getInstance().logInfo("Too many producers! Removed a producer.");
            }

        }
//    }

    /**
     * Handles log events to display them on the GUI.
     * @param message Log message
     * Implements LogEventListerInterface
     */
    @Override
    public void onLogEvent(String message) {
        String existingText = productionRegulatorGUI.getTextArea().getText();
        productionRegulatorGUI.getTextArea().setText(message + "\n" + existingText);

        // Setting the scroll to top always, so it does not move to the bottom when new logs get appended.
        productionRegulatorGUI.getTextArea().setCaretPosition(0);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}


