package org.juhnkim.models;
import org.juhnkim.services.Consumer;
import org.juhnkim.services.Producer;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class State implements Serializable {
    private int numProducers;
    private int numConsumers;
    private List<Producer> producerList;
    private List<Consumer> consumerList;
    private List<Integer> producerIntervals;
    private List<Integer> consumerIntervals;

    public State(LinkedList<Producer> producerList, LinkedList<Consumer> consumerList,
                 List<Integer> producerIntervals, List<Integer> consumerIntervals) {

        this.producerList = producerList;
        this.consumerList = consumerList;
        this.producerIntervals = producerIntervals;
        this.consumerIntervals = consumerIntervals;
    }

    public int getNumProducers() {
        return numProducers;
    }

    public void setNumProducers(int numProducers) {
        this.numProducers = numProducers;
    }

    public int getNumConsumers() {
        return numConsumers;
    }

    public void setNumConsumers(int numConsumers) {
        this.numConsumers = numConsumers;
    }
}
