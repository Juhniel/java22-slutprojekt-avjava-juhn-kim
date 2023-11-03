package org.juhnkim.services;

import org.juhnkim.utils.Log;

public class LogService {
    private final Buffer buffer;

    public LogService(Buffer buffer) {
        this.buffer = buffer;
    }
    public void logConsumedToProducerRatio() {
        if (buffer.getProducedMessages() == 0) {
            Log.getInstance().logInfo("No data for average");
            return;
        }

        Log.getInstance().logInfo("Average Consumed Messages: " + String.format("%.2f", buffer.consumedRatio()) + "%");
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
