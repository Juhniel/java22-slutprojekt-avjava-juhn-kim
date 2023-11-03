package org.juhnkim.threads;

import org.juhnkim.models.Consumer;
import org.juhnkim.services.Buffer;

import java.io.Serializable;

/**
 * Runnable implementation for a Consumer thread that consumes messages at a fixed interval.
 */
public class ConsumerThread implements Runnable, Serializable {
    private final Buffer buffer;
    private final Consumer consumer;
    private boolean isRunning;

    public ConsumerThread(Buffer buffer, Consumer consumer) {
        this.buffer = buffer;
        this.consumer = consumer;
        this.isRunning = true;
    }

    public void stop() {
        isRunning = false;
        Thread.currentThread().interrupt();
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