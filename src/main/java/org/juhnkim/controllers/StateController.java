package org.juhnkim.controllers;

import org.juhnkim.models.Message;
import org.juhnkim.models.State;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.Consumer;
import org.juhnkim.services.Producer;
import org.juhnkim.services.StateService;
import org.juhnkim.utils.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StateController {
    private final LinkedList<Producer> producerLinkedList;
    private final List<Consumer> consumerList;
    private final Buffer buffer;
    private LinkedList<Integer> producerIntervals;
    private List<Integer> consumerIntervals;
    private List<Message> allMessagesInBuffer;

    public StateController(Buffer buffer) {
        this.producerLinkedList = new LinkedList<>();
        this.consumerList = new ArrayList<>();
        this.buffer = buffer;
        this.producerIntervals = new LinkedList<>();
        this.consumerIntervals = new ArrayList<>();
        this.allMessagesInBuffer = new ArrayList<>();
    }

    public void saveCurrentState() {
        for (Producer p : producerLinkedList) {
            producerIntervals.add(p.getProducerInterval());
        }


        for (Consumer c : consumerList) {
            consumerIntervals.add(c.getConsumerInterval());
        }

        allMessagesInBuffer = buffer.getAllMessagesInBuffer();

        State state = new State(producerLinkedList, consumerList, producerIntervals, consumerIntervals, allMessagesInBuffer);
        StateService stateService = new StateService();
        stateService.saveState(state);
    }


    public void loadSavedState() {
        StateService stateService = new StateService();
        State state = stateService.loadState();

        if (state != null) {
            // Clear current Producers and Consumers
            producerLinkedList.clear();
            consumerList.clear();
            buffer.clear();

            // Load Producers
            producerIntervals = state.getProducerIntervals();
            for (Integer producerInterval : producerIntervals) {
                Producer producer = new Producer(buffer);
                producer.setProducerInterval(producerInterval);
                producerLinkedList.add(producer);
                new Thread(producer).start();
            }

            // Load Consumers
            consumerIntervals = state.getConsumerIntervals();
            for (Integer consumerInterval : consumerIntervals) {
                Consumer consumer = new Consumer(buffer);
                consumer.setConsumerInterval(consumerInterval);
                consumerList.add(consumer);
                new Thread(consumer).start();
            }

            // Load Messages
            allMessagesInBuffer = state.getMessageList();
            buffer.setAllMessagesInBuffer(allMessagesInBuffer);

            Log.getInstance().logInfo("State loaded successfully.");
            Log.getInstance().logInfo("Amount of Producers loaded: " + state.getProducerList().size());
            Log.getInstance().logInfo("Amount of Consumers loaded: " + state.getConsumerList().size());
            Log.getInstance().logInfo("Amount of Messages in queue loaded: " + state.getMessageList().size());
        } else {
            Log.getInstance().logInfo("Failed to load state.");
        }
    }
}
