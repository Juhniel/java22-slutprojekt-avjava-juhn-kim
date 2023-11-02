package org.juhnkim.threads;

import org.juhnkim.models.Message;
import org.juhnkim.models.Producer;
import org.juhnkim.services.Buffer;
import org.juhnkim.utils.Log;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProducerThread implements Runnable {
    private final Buffer buffer;
    private final Producer producer;
    private volatile boolean isRunning;


    public ProducerThread(Buffer buffer, Producer producer) {
        this.buffer = buffer;
        this.producer = producer;
        this.isRunning = true;
    }

    public void stop() {
        isRunning = false;
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        String text = "Random text";
        Log.getInstance().logInfo("Producer Thread - Starting");
        Log.getInstance().logInfo("Producer will sleep for " + producer.getProducerInterval() + " milliseconds before producing the next message");
        while (isRunning) {
            try {
                Thread.sleep(producer.getProducerInterval());
                buffer.add(new Message(text, LocalDate.now(), LocalTime.now()));
                System.out.println("Producing");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

