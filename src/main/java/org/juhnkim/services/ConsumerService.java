package org.juhnkim.services;

import org.juhnkim.models.Consumer;
import org.juhnkim.models.State;
import org.juhnkim.threads.ConsumerThread;

public class ConsumerService {
    private final Buffer buffer;
    private final State state;

    public ConsumerService(Buffer buffer, State state) {
        this.buffer = buffer;
        this.state = state;
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
}
