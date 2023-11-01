package org.juhnkim.services;

import org.juhnkim.models.Message;
import org.juhnkim.models.Producer;
import org.juhnkim.utils.Log;

import java.time.LocalDate;
import java.time.LocalTime;

public class ProducerService implements Runnable {
    private final Buffer buffer;
    private final Producer producer;
    volatile boolean isRunning = true;


    public ProducerService(Buffer buffer, Producer producer) {
        this.buffer = buffer;
        this.producer = producer;
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        String text = "Random text";
        Log.getInstance().logInfo("Producer will sleep for " + producer.getProducerInterval() +" milliseconds before producing the next message");
        while (isRunning) {
            try {
                Thread.sleep(producer.getProducerInterval());
                buffer.add(new Message(text, LocalDate.now(), LocalTime.now()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

