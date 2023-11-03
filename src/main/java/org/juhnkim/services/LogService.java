package org.juhnkim.services;

import org.juhnkim.utils.Log;

/**
 * Provides logging services for buffer-related activities, specifically focusing on the ratios
 * of consumed to produced messages and balance percentage warnings.
 */
public class LogService {
    private final Buffer buffer;
    private final ProducerService producerService;

    public LogService(Buffer buffer, ProducerService producerService) {
        this.buffer = buffer;
        this.producerService = producerService;
    }


    /**
     * Logs the ratio of consumed messages to produced messages. If no messages have been produced,
     * logs that there is no data. It also resets the message counts in the buffer to allow for
     * fresh calculations next time this method is called.
     */
    public void logConsumedToProducerRatio() {
        if (buffer.getProducedMessages() == 0) {
            Log.getInstance().logInfo("No data for average");
            return;
        }

        // Log the average consumed messages in percentage format.
        Log.getInstance().logInfo("Average Consumed Messages: " + String.format("%.2f", buffer.consumedRatio()) + "%");

        // Reset the produced and consumed message counters.
        buffer.setProducedMessages(0);
        buffer.setConsumedMessages(0);
    }

    /**
     * Logs warnings related to the producer balance percentage. Warns if the percentage is below or
     * above certain thresholds, suggesting an imbalance in the number of producers.
     *
     * @param balancePercentage The current percentage fill of the buffer to evaluate the balance.
     */
    public void logProducerWarnings(double balancePercentage) {
        if(producerService.getProducerThreadList().isEmpty()) {
            return;
        }
        if (balancePercentage <= 10) {
            Log.getInstance().logInfo("Amount of producers too low!");
        } else if (balancePercentage >= 90) {
            Log.getInstance().logInfo("Amount of producers too high!");
        }
    }
}
