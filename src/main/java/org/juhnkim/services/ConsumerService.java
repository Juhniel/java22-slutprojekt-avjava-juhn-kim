package org.juhnkim.services;

import org.juhnkim.models.Consumer;

import java.io.Serializable;
import java.util.Random;

public class ConsumerService implements Runnable, Serializable {
    private final Buffer buffer;
    private final Consumer consumer;
    boolean isRunning = true;

    public ConsumerService(Buffer buffer, Consumer consumer) {
        this.buffer = buffer;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(consumer.getConsumerInterval());
                buffer.remove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}