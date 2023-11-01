package org.juhnkim.controllers;

import org.juhnkim.models.Consumer;
import org.juhnkim.services.Buffer;

import java.util.ArrayList;
import java.util.List;


public class ConsumerController {
    private final Buffer buffer;
    private final List<Consumer> consumerList;
    private final List<Integer> consumerIntervalList;

    public ConsumerController(Buffer buffer) {
        this.buffer = buffer;
        this.consumerList = new ArrayList<>();
        this.consumerIntervalList = new ArrayList<>();
    }

    public void initConsumers() {
        int numConsumers = (int) (Math.random() * 13) + 3;
        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer(buffer);
            consumerList.add(consumer);
            consumerIntervalList.add(consumer.consumerInterval);
            new Thread(consumer).start();
        }
        System.out.println("Consumers: " + consumerList.size());
    }



    public List<Consumer> getConsumerList() {
        return consumerList;
    }

    public List<Integer> getConsumerIntervalList() {
        return consumerIntervalList;
    }
}
