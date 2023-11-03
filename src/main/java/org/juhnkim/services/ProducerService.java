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
    private final AutoAdjustService autoAdjustService;
    private boolean isAutoAdjustOn;
    private final javax.swing.Timer autoAdjustTimer;
    private final LinkedList<ProducerThread> producerThreadList;

    /**
     * Manages the lifecycle and auto-adjustment of producer threads within the system.
     * It interacts with the GUI, the buffer, and maintains a list of active producer threads,
     * offering methods to add, remove, and restart producer threads as well as toggling
     * the auto-adjust feature based on system performance.
     */
    public ProducerService(ProductionRegulatorGUI productionRegulatorGUI, AutoAdjustService autoAdjustService,  Buffer buffer, State state) {
        this.state = state;
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.autoAdjustService = autoAdjustService;
        this.buffer = buffer;
        this.producerThreadList = new LinkedList<>();
        this.propertyChangeService = new PropertyChangeService(productionRegulatorGUI, buffer);
        this.autoAdjustTimer = new javax.swing.Timer(3000, e -> autoAdjustService.autoAdjustProducers(this, propertyChangeService));
    }

    /**
     * Adds a producer thread to the system and starts it.
     */
    public void addProducer() {
        Producer producer = new Producer();
        state.getProducerList().add(producer);

        ProducerThread producerThread = new ProducerThread(buffer, producer);
        producerThreadList.add(producerThread);
        new Thread(producerThread).start();

        Log.getInstance().logInfo("Producer Added");
        Log.getInstance().logInfo("Producer Count: " + state.getProducerList().size());
    }

    /**
     * Removes the most recently added producer thread from the system.
     */
    public void removeProducer() {
        if (!producerThreadList.isEmpty()) {
            ProducerThread lastProducerThread = producerThreadList.removeLast();
            lastProducerThread.stop();

            if (!state.getProducerList().isEmpty()) {
                state.getProducerList().removeLast();
            }

            Log.getInstance().logInfo("Producer Removed");
            Log.getInstance().logInfo("Producer Count: " + state.getProducerList().size());
        }
    }

    /**
     * Restarts all producer threads, first stopping them all and then starting them anew.
     */
    public void restartProducerThreads() {
        Log.getInstance().logInfo("Stopping all producers before restart.");
        stopAllProducers();

        if (state.getProducerList().isEmpty()) {
            Log.getInstance().logInfo("No producers to start.");
        }
        for (Producer producer : state.getProducerList()) {
            ProducerThread producerThread = new ProducerThread(buffer, producer);
            new Thread(producerThread).start();
            producerThreadList.add(producerThread);
            Log.getInstance().logInfo("Started producer thread with interval: " + producer.getProducerInterval());
        }
        Log.getInstance().logInfo("All producer threads restarted.");
    }

    /**
     * Stops all producer threads that are currently running.
     */
    private void stopAllProducers() {
        Log.getInstance().logInfo("Stopping producer thread.");
        for (ProducerThread producerThread : producerThreadList) {
            producerThread.stop();
        }
        Log.getInstance().logInfo("All producer threads have been signaled to stop.");
    }


    /**
     * Toggles the auto-adjust feature on or off. If on, it will auto-adjust producers at a
     * regular interval defined by an internal timer.
     */
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
