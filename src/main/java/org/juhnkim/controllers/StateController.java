package org.juhnkim.controllers;

import org.juhnkim.models.State;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.StateService;
import org.juhnkim.utils.Log;


public class StateController {
    private final Buffer buffer;
    private final State state;
    private final StateService stateService;

    public StateController(Buffer buffer) {
        this.buffer = buffer;
        this.state = new State();
        this.stateService = new StateService();
    }

    // Method to trigger saving the current state of the application
    public void saveCurrentState() {
        try {
            stateService.saveApplicationState(
                    state.getProducerList(),
                    state.getConsumerList(),
                    buffer.getAllMessagesInBuffer()
            );
            Log.getInstance().logInfo("Current application state saved successfully.");
        } catch (Exception e) {
            Log.getInstance().logError("Failed to save current application state: " + e.getMessage());
        }
    }

    // Method to trigger loading the saved state of the application
    public void loadSavedState() {
        try {
            State loadedState = stateService.loadApplicationState();
            if (loadedState != null) {
                // Restore the state here or pass it to where it needs to go
                this.state = loadedState;
                // Here you might want to restart producers/consumers, update the GUI, etc.
                Log.getInstance().logInfo("Application state loaded successfully.");
            } else {
                Log.getInstance().logError("No saved state to load.");
            }
        } catch (Exception e) {
            Log.getInstance().logError("Failed to load saved application state: " + e.getMessage());
        }
    }




//    public void saveCurrentState() {
//
//        for (Producer p : state.getProducerList()) {
//            state.getProducerList().add(p);
//        }
//
//        for (Consumer c : state.getConsumerList()) {
//            state.getConsumerList().add(c);
//        }
//
//        for (Message m : buffer.getAllMessagesInBuffer()) {
//            state.getMessageList().add(m);
//        }
//
//        stateService.saveState(state);
//    }


//    public void loadSavedState() {
//        state = stateService.loadState();
//
//        if (state != null) {
//            // Clear current Producers, Consumers and Messages
//            state.getProducerList().clear();
//            state.getConsumerList().clear();
//            state.getMessageList().clear();
//            buffer.clear();
//
//
//            for (Producer p : state.getProducerList()) {
//
//            }
//

//            // Populate producers based on saved state
//            for (Integer producerInterval : state.getProducerIntervals()) {
//                Producer newProducer = new Producer(buffer);
//                newProducer.setProducerInterval(producerInterval);
//                producerLinkedList.add(newProducer);
//                new Thread(newProducer).start();
//            }
//
//            // Populate consumers based on saved state
//            for (Integer consumerInterval : state.getConsumerIntervals()) {
//                Consumer newConsumer = new Consumer(buffer);
//                newConsumer.setConsumerInterval(consumerInterval);
//                consumerList.add(newConsumer);
//                new Thread(newConsumer).start();
//            }
//
//            // Populate buffer based on saved state
//            buffer.setAllMessagesInBuffer(state.getMessageList());
//
////             Log information about the loaded state
//            Log.getInstance().logInfo("State loaded successfully.");
//            Log.getInstance().logInfo("Amount of Producers loaded: " + producerLinkedList.size());
//            Log.getInstance().logInfo("Amount of Consumers loaded: " + consumerList.size());
//            Log.getInstance().logInfo("Amount of Messages in queue loaded: " + state.getMessageList().size());
//        } else {
//            Log.getInstance().logInfo("Failed to load state.");
//        }
//    }
}
