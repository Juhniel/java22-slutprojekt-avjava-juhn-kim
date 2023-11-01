package org.juhnkim.controllers;

import org.juhnkim.models.Consumer;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.ConsumerService;

import java.util.ArrayList;
import java.util.List;


public class ConsumerController {
    private final Buffer buffer;
    private final List<Consumer> consumerList;


    public ConsumerController(Buffer buffer) {
        this.buffer = buffer;
        this.consumerList = new ArrayList<>();
    }

    public void initConsumers() {
        int numConsumers = (int) (Math.random() * 13) + 3;
        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer();
            ConsumerService consumerService = new ConsumerService(buffer, consumer);
            consumerList.add(consumer);
            new Thread(consumerService).start();
        }
        System.out.println("Consumers: " + consumerList.size());
    }



    public List<Consumer> getConsumerList() {
        return consumerList;
    }

}
