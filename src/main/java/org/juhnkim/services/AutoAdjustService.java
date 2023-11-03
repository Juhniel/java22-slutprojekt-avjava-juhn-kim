package org.juhnkim.services;

import org.juhnkim.models.Consumer;
import org.juhnkim.models.Producer;
import org.juhnkim.models.State;
import org.juhnkim.utils.Log;

import java.util.LinkedList;
import java.util.List;

public class AutoAdjustService {

    private final Buffer buffer;

    private final State state;


    public AutoAdjustService(Buffer buffer, State state) {
        this.buffer = buffer;
        this.state = state;
    }

    /**
     * Automatically adjusts the number of producer threads based on buffer status and consumer speed.
     *
     * @param producerService        the service managing producer threads
     * @param propertyChangeService  the service monitoring buffer property changes
     */
    public void autoAdjustProducers(ProducerService producerService, PropertyChangeService propertyChangeService) {
        double averageConsumerInterval = calculateAvgConsumerInterval();
        double averageProducerInterval = calculateAvgProducerInterval();
        double consumedRatio = buffer.consumedRatio();

        // Add a producer if there are none.
        if(producerService.getProducerThreadList().isEmpty()) {
            producerService.addProducer();
            return;
        }

        final double targetFillPercentage = 50.0;
        double currentFillPercentage = propertyChangeService.getBalancePercentage();

        // Define upper and lower bounds for the buffer's fill percentage
        double pBarHigher = targetFillPercentage*1.10;
        double pBarLower = targetFillPercentage*0.90;

        // Adjust producer count based on the current fill percentage and consumed ratio.
        if (currentFillPercentage < pBarLower && consumedRatio >= 100) {
            producerService.addProducer();

        } else if(currentFillPercentage > pBarHigher && consumedRatio < 100) {
            producerService.removeProducer();
        }
    }

    /**
     * Calculates the average interval between consumer actions.
     *
     * @return the average consumer interval
     */
    private double calculateAvgConsumerInterval() {
        double averageConsumerInterval = 0;
        List<Consumer> consumerList = state.getConsumerList();

        if (consumerList.isEmpty()) return 0;

        for(Consumer c : consumerList) {
            averageConsumerInterval += c.getConsumerInterval();
        }

        return averageConsumerInterval/state.getConsumerList().size();
    }

    /**
     * Calculates the average interval between producer actions.
     *
     * @return the average producer interval
     */
    private double calculateAvgProducerInterval() {
        double averageProducerInterval = 0;
        LinkedList<Producer> producerList = state.getProducerList();

        if (producerList.isEmpty()) return 0;

        for(Producer p : producerList) {
            averageProducerInterval += p.getProducerInterval();
        }

        return averageProducerInterval/state.getProducerList().size();
    }
}
