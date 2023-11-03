package org.juhnkim.services;

import org.juhnkim.models.Consumer;
import org.juhnkim.models.State;
import org.juhnkim.threads.ConsumerThread;
import org.juhnkim.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages consumer lifecycle within the system. It handles initialization and restarting of consumer threads
 * that are responsible for consuming messages from a shared buffer.
 */
public class ConsumerService {
    private final Buffer buffer;
    private final State state;
    private final List<ConsumerThread> consumerThreadList;

    public ConsumerService(Buffer buffer, State state) {
        this.buffer = buffer;
        this.state = state;
        this.consumerThreadList = new ArrayList<>();
    }

    /**
     * Initializes a random number of consumer threads between 3 and 15 and starts them.
     * Consumers are added to the system's state for tracking.
     */
    public void initConsumers() {
        int numConsumers = (int) (Math.random() * 13) + 3;
        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer();
            ConsumerThread consumerThread = new ConsumerThread(buffer, consumer);
            state.getConsumerList().add(consumer);
            new Thread(consumerThread).start();
        }
        System.out.println("Consumers: " + state.getConsumerList().size());
    }

    /**
     * Stops all consumer threads and restarts them. This can be used to refresh the consumer threads
     * in response to changes in the system state or buffer.
     */
    public void restartConsumerThreads() {
        stopAllConsumers();
        Log.getInstance().logInfo("Consumer Thread Stopped");

        for (Consumer consumer : state.getConsumerList()) {
            ConsumerThread ct = new ConsumerThread(buffer, consumer);
            new Thread(ct).start();
            Log.getInstance().logInfo("Consumer Thread Started");
            consumerThreadList.add(ct);
        }
    }

    /**
     * Stops all consumer threads.
     */
    private void stopAllConsumers() {
        for (ConsumerThread consumerThread : consumerThreadList) {
            consumerThread.stop();
        }
    }
}
