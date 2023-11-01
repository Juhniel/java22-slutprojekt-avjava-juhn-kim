package org.juhnkim.controllers;

import org.juhnkim.models.Message;
import org.juhnkim.models.State;
import org.juhnkim.services.Buffer;
import org.juhnkim.models.Consumer;
import org.juhnkim.models.Producer;
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

    private State state;
    private StateService stateService;

    public StateController(Buffer buffer) {
        this.producerLinkedList = new LinkedList<>();
        this.consumerList = new ArrayList<>();
        this.buffer = buffer;
        this.producerIntervals = new LinkedList<>();
        this.consumerIntervals = new ArrayList<>();
        this.allMessagesInBuffer = new ArrayList<>();
        this.state = new State(producerLinkedList, consumerList, producerIntervals, consumerIntervals, allMessagesInBuffer);
        this.stateService = new StateService();
    }

    public void saveCurrentState() {

        for (Producer p : state.getProducerList()) {
            state.getProducerIntervals().add(p.getProducerInterval());
        }

        for (Consumer c : state.getConsumerList()) {
            state.getConsumerIntervals().add(c.consumerInterval);
        }

        allMessagesInBuffer = buffer.getAllMessagesInBuffer();
        stateService.saveState(state);
    }


    public void loadSavedState() {
        state = stateService.loadState();

        if (state != null) {
            // Clear current Producers and Consumers
            producerLinkedList.clear();
            consumerList.clear();
            buffer.clear();

            // Populate producers based on saved state
            for (Integer producerInterval : state.getProducerIntervals()) {
                Producer newProducer = new Producer(buffer);
                newProducer.setProducerInterval(producerInterval);
                producerLinkedList.add(newProducer);
                new Thread(newProducer).start();
            }

            // Populate consumers based on saved state
            for (Integer consumerInterval : state.getConsumerIntervals()) {
                Consumer newConsumer = new Consumer(buffer);
                newConsumer.setConsumerInterval(consumerInterval);
                consumerList.add(newConsumer);
                new Thread(newConsumer).start();
            }

            // Populate buffer based on saved state
            buffer.setAllMessagesInBuffer(state.getMessageList());

            // Log information about the loaded state
//            Log.getInstance().logInfo("State loaded successfully.");
//            Log.getInstance().logInfo("Amount of Producers loaded: " + producerLinkedList.size());
//            Log.getInstance().logInfo("Amount of Consumers loaded: " + consumerList.size());
//            Log.getInstance().logInfo("Amount of Messages in queue loaded: " + state.getMessageList().size());
        } else {
            Log.getInstance().logInfo("Failed to load state.");
        }
    }
}
