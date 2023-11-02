package org.juhnkim.models;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
