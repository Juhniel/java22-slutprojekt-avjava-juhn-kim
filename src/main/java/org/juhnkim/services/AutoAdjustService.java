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

    public void autoAdjustProducers(ProducerService producerService, PropertyChangeService propertyChangeService) {
        double averageConsumerInterval = calculateAvgConsumerInterval();
        double averageProducerInterval = calculateAvgProducerInterval();
        double consumedRatio = buffer.consumedRatio();

        if(producerService.getProducerThreadList().isEmpty()) {
            producerService.addProducer();
            return;
        }

        Log.getInstance().logInfo("Average ConsumerInterval: " + averageConsumerInterval);
        Log.getInstance().logInfo("Average ProducerInterval: " + averageProducerInterval);

        // Get the target buffer fill percentage and current buffer status
        final double targetFillPercentage = 50.0; // buffer target fill rate
        double currentFillPercentage = propertyChangeService.getBalancePercentage();

        Log.getInstance().logInfo("CurrentFillPercentage: " + currentFillPercentage);
        double pBarHigher = targetFillPercentage*1.10;
        double pBarLower = targetFillPercentage*0.90;

        Log.getInstance().logInfo("pBarHigher: " + pBarHigher);
        Log.getInstance().logInfo("pBarLower: " + pBarLower);

        // Kolla först om currentFill är runt 50
        if (currentFillPercentage < pBarLower && consumedRatio >= 100) { // OM progressbar är MINDRE än 40 & consumer konsumerar mer än 100%
            producerService.addProducer();

        } else if(currentFillPercentage > pBarHigher && consumedRatio < 100) {
            producerService.removeProducer();
        }

        Log.getInstance().logInfo("Current Consumer Producer: " + state.getProducerList().size());
        Log.getInstance().logInfo("Current Consumer Count: " + state.getConsumerList().size());
    }





    private double calculateAvgConsumerInterval() {
        double averageConsumerInterval = 0;
        List<Consumer> consumerList = state.getConsumerList();

        for(Consumer c : consumerList) {
            averageConsumerInterval += c.getConsumerInterval();
        }

        return averageConsumerInterval/state.getConsumerList().size();
    }

    private double calculateAvgProducerInterval() {
        double averageProducerInterval = 0;
        LinkedList<Producer> producerList = state.getProducerList();

        for(Producer p : producerList) {
            averageProducerInterval += p.getProducerInterval();
        }

        return averageProducerInterval/state.getProducerList().size();
    }
}
