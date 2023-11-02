package org.juhnkim.services;

import org.juhnkim.models.Producer;
import org.juhnkim.models.State;
import org.juhnkim.threads.ProducerThread;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import java.util.LinkedList;

public class ProducerService {
    private final State state;
    private final Buffer buffer;
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private final PropertyChangeService propertyChangeService;
    private boolean isAutoAdjustOn;
    private final javax.swing.Timer autoAdjustTimer;
    private final LinkedList<ProducerThread> producerThreadList;


    public ProducerService(ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer, State state) {
        this.state = state;
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.buffer = buffer;
        this.producerThreadList = new LinkedList<>();
        this.propertyChangeService = new PropertyChangeService(productionRegulatorGUI, buffer);
        this.autoAdjustTimer = new javax.swing.Timer(4000, e -> autoAdjustProducers());
    }

    public void autoAdjustProducers() {
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
        // Here we create the thread that will run the producer
        ProducerThread producerThread = new ProducerThread(buffer, producer);
        // Add the producer thread to the list of running threads
        producerThreadList.add(producerThread);
        // Now start the thread
        new Thread(producerThread).start();

        // Add the producer to the state for state tracking, not thread management
        state.getProducerList().add(producer);

        Log.getInstance().logInfo("Producer Added");
        Log.getInstance().logInfo("Producer Count: " + state.getProducerList().size());
    }

    public void removeProducer() {
        if (!producerThreadList.isEmpty()) {
            // Stop the last thread
            ProducerThread lastProducerThread = producerThreadList.removeLast();
            lastProducerThread.stop();

            // Now remove the producer from the state
            if (!state.getProducerList().isEmpty()) {
                state.getProducerList().removeLast();
            }

            Log.getInstance().logInfo("Producer Removed");
            Log.getInstance().logInfo("Producer Count: " + state.getProducerList().size());
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
