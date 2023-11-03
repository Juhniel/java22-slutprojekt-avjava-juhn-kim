package org.juhnkim.services;

import org.juhnkim.models.Consumer;
import org.juhnkim.models.Producer;
import org.juhnkim.models.State;
import org.juhnkim.threads.ConsumerThread;
import org.juhnkim.threads.ProducerThread;

import java.util.ArrayList;
import java.util.List;

public class ConsumerService {
    private final Buffer buffer;
    private final State state;
    private final List<ConsumerThread> consumerThreadList;

    public ConsumerService(Buffer buffer, State state) {
        this.buffer = buffer;
        this.state = state;
        this.consumerThreadList = new ArrayList<>();
    }

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

    public void restartConsumerThreads() {
        stopAllConsumers();

        // Create and start a new thread for each producer
        for (Consumer consumer : state.getConsumerList()) {
            ConsumerThread ct = new ConsumerThread(buffer, consumer);
            new Thread(ct).start();
            consumerThreadList.add(ct);
        }
    }

    private void stopAllConsumers() {
        for (ConsumerThread consumerThread : consumerThreadList) {
            consumerThread.stop();
        }
    }
}
