package org.juhnkim.services;

import org.juhnkim.models.Producer;
import org.juhnkim.models.State;
import org.juhnkim.threads.ProducerThread;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import java.util.LinkedList;
import java.util.List;

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

    public void addProducer() {
        Producer producer = new Producer();
        state.getProducerList().add(producer);
        ProducerThread producerThread = new ProducerThread(buffer, producer);
        producerThreadList.add(producerThread);
        new Thread(producerThread).start();

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

    public void restartProducerThreads(State loadedState) {
        Log.getInstance().logInfo("Stopping all producers before restart.");
        Log.getInstance().logInfo("inside restartProducerThreads(): " + loadedState.getProducerList().size());

        if (state.getProducerList().isEmpty()) {
            Log.getInstance().logInfo("No producers to start.");
        }
        for (Producer producer : loadedState.getProducerList()) {
            ProducerThread producerThread = new ProducerThread(buffer, producer);
            new Thread(producerThread).start();
            producerThreadList.add(producerThread);
            Log.getInstance().logInfo("Started producer thread with interval: " + producer.getProducerInterval());
        }
        Log.getInstance().logInfo("All producer threads restarted.");
    }

    public void stopAllProducers() {
        Log.getInstance().logInfo("Stopping producer thread.");
        for (ProducerThread producerThread : producerThreadList) {
            producerThread.stop();
        }
        Log.getInstance().logInfo("All producer threads have been signaled to stop.");
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

    public void toggleAutoAdjust() {
        isAutoAdjustOn = !isAutoAdjustOn;
        productionRegulatorGUI.autoAdjustColor(isAutoAdjustOn);
        if (isAutoAdjustOn) {
            autoAdjustTimer.start();
        } else {
            autoAdjustTimer.stop();
        }
    }

    public LinkedList<ProducerThread> getProducerThreadList() {
        return producerThreadList;
    }
}
