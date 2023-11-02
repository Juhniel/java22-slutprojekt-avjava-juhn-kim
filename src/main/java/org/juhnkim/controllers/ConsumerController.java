package org.juhnkim.controllers;

import org.juhnkim.models.Consumer;
import org.juhnkim.services.Buffer;
import org.juhnkim.threads.ConsumerThread;

import java.util.List;


public class ConsumerController {
    private final Buffer buffer;

    public ConsumerController(Buffer buffer) {
        this.buffer = buffer;
    }

    public void initConsumers() {
        int numConsumers = (int) (Math.random() * 13) + 3;
        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer();
            ConsumerThread consumerThread = new ConsumerThread(buffer, consumer);
            consumerList.add(consumer);
            new Thread(consumerThread).start();
        }
        System.out.println("Consumers: " + consumerList.size());
    }



    public List<Consumer> getConsumerList() {
        return consumerList;
    }

}
