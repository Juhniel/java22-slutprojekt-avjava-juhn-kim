package org.juhnkim.models;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the state of the system with lists of producers, consumers, and messages.
 * This class is serializable to allow saving and loading of the system state.
 */
public class State implements Serializable {

    private LinkedList<Producer> producerList;
    private List<Consumer> consumerList;
    private List<Message> messageList;

    public State() {
        this.producerList = new LinkedList<>();
        this.consumerList = new ArrayList<>();
        this.messageList = new ArrayList<>();
    }

    public State(LinkedList<Producer> producerList,
                 List<Consumer> consumerList,
                 List<Message> messageList) {

        this.producerList = producerList;
        this.consumerList = consumerList;
        this.messageList = messageList;
    }

    /**
     * Updates the state with the values from a new state object.
     *
     * @param newState the new state to copy values from
     */
    public void updateState(State newState) {
        setProducerList(newState.getProducerList());
        setConsumerList(newState.getConsumerList());
        setMessageList(newState.getMessageList());
    }

    public LinkedList<Producer> getProducerList() {
        return producerList;
    }

    public void setProducerList(LinkedList<Producer> producerList) {
        this.producerList = producerList;
    }

    public List<Consumer> getConsumerList() {
        return consumerList;
    }

    public void setConsumerList(List<Consumer> consumerList) {
        this.consumerList = consumerList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
