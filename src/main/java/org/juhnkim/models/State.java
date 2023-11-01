package org.juhnkim.models;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class State implements Serializable {

    private int numProducers;
    private int numConsumers;
    private final LinkedList<Producer> producerList;
    private final List<Consumer> consumerList;
    private final LinkedList<Integer> producerIntervals;
    private final List<Integer> consumerIntervals;
    private final List<Message> messageList;

    public State(LinkedList<Producer> producerList,
                 List<Consumer> consumerList,
                 LinkedList<Integer> producerIntervals,
                 List<Integer> consumerIntervals,
                 List<Message> messageList) {

        this.producerList = producerList;
        this.consumerList = consumerList;
        this.producerIntervals = producerIntervals;
        this.consumerIntervals = consumerIntervals;
        this.messageList = messageList;
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

    public List<Producer> getProducerList() {
        return producerList;
    }

    public List<Consumer> getConsumerList() {
        return consumerList;
    }

    public LinkedList<Integer> getProducerIntervals() {
        return producerIntervals;
    }

    public List<Integer> getConsumerIntervals() {
        return consumerIntervals;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

}
