package org.juhnkim.controllers;

import org.juhnkim.services.Buffer;
import org.juhnkim.utils.Log;

public class LogController {

    private final Buffer buffer;

    public LogController(Buffer buffer) {
        this.buffer = buffer;
    }

    public void logConsumedToProducerRatio() {
        if (buffer.getProducedMessages() == 0) {
            Log.getInstance().logInfo("No data for average");
            return;
        }

        double consumedRatio = ((double) buffer.getConsumedMessages() / buffer.getProducedMessages()) * 100;
        Log.getInstance().logInfo("Average Consumed Messages: " + String.format("%.2f", consumedRatio) + "%");
        buffer.setProducedMessages(0);
        buffer.setConsumedMessages(0);
    }

    public void logProducerWarnings(double balancePercentage) {
        if (balancePercentage <= 10) {
            Log.getInstance().logInfo("Amount of producers too low!");
        } else if (balancePercentage >= 90) {
            Log.getInstance().logInfo("Amount of producers too high!");
        }
    }
}
