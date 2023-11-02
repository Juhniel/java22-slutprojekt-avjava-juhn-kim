package org.juhnkim.threads;

import org.juhnkim.models.Consumer;
import org.juhnkim.services.Buffer;

import java.io.Serializable;

public class ConsumerThread implements Runnable, Serializable {
    private final Buffer buffer;
    private final Consumer consumer;
    boolean isRunning = true;

    public ConsumerThread(Buffer buffer, Consumer consumer) {
        this.buffer = buffer;
        this.consumer = consumer;
    }

    public void stop() {
        isRunning = false;
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