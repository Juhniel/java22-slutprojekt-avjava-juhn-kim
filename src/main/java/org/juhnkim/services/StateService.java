package org.juhnkim.services;

import org.juhnkim.models.Consumer;
import org.juhnkim.models.Message;
import org.juhnkim.models.Producer;
import org.juhnkim.models.State;
import org.juhnkim.utils.Log;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages the saving and loading of application state, providing persistence across sessions.
 * It serializes the state of producers, consumers, and messages into a file and retrieves them.
 */
public class StateService {

    private static final String STATE_FILE_PATH = "files/state.dat";
    private final ProducerService producerService;
    private final ConsumerService consumerService;
    private final Buffer buffer;
    private State state;

    public StateService(ProducerService producerService, ConsumerService consumerService, State state, Buffer buffer) {
        this.producerService = producerService;
        this.consumerService = consumerService;
        this.state = state;
        this.buffer = buffer;
    }

    /**
     * Saves the current application state to a file.
     */
    public synchronized void saveApplicationState() {
        state = createState(state.getProducerList(), state.getConsumerList(), buffer.getAllMessagesInBuffer());

        try {
            saveStateToFile(state);
            Log.getInstance().logInfo("Current application state saved successfully.");
            Log.getInstance().logInfo("Producers: " + state.getProducerList().size());
            Log.getInstance().logInfo("Consumers: " + state.getConsumerList().size());
            Log.getInstance().logInfo("Messages: " + state.getMessageList().size());
        } catch (Exception e) {
            Log.getInstance().logError("Failed to save current application state: " + e.getMessage());
        }
    }

    /**
     * Loads the application state from a file.
     */
    public synchronized void loadApplicationState() {
        try {
            State loadedState = loadStateFromFile();
            if (loadedState != null) {
                for (Producer producer : state.getProducerList()) {
                    Log.getInstance().logInfo("Loaded Producer: " + producer.toString());
                }

                clearCurrentState(state, buffer);
                state.updateState(loadedState);
                buffer.setAllMessagesInBuffer(state.getMessageList());
                restartThreads();

                Log.getInstance().logInfo("Application state loaded successfully.");
                Log.getInstance().logInfo("Producers: " + state.getProducerList().size());
                Log.getInstance().logInfo("Consumers: " + state.getConsumerList().size());
                Log.getInstance().logInfo("Messages: " + state.getMessageList().size());
            } else {
                Log.getInstance().logError("No saved state to load.");
            }
        } catch (Exception e) {
            Log.getInstance().logError("Failed to load saved application state: " + e.getMessage());
        }
    }

    // Helper method to create a State object
    private State createState(List<Producer> producers, List<Consumer> consumers, List<Message> messages) {
        State state = new State();
        state.setProducerList(new LinkedList<>(producers));
        state.setConsumerList(new LinkedList<>(consumers));
        state.setMessageList(new LinkedList<>(messages));
        return state;
    }

    /**
     * Clears the current application state.
     */
    private void clearCurrentState(State state, Buffer buffer) {
        state.getProducerList().clear();
        state.getConsumerList().clear();
        Log.getInstance().logInfo("Buffer size: " + buffer.getAllMessagesInBuffer().size());
        buffer.clear();
        Log.getInstance().logInfo("Buffer size: " + buffer.getAllMessagesInBuffer().size());
        Log.getInstance().logInfo("Current state cleared successfully.");
    }

    /**
     * Restarts all producer and consumer threads.
     */
    private void restartThreads() {
        producerService.restartProducerThreads();
        consumerService.restartConsumerThreads();
    }


    /**
     * Serializes the State object and writes it to a file.
     */
    private void saveStateToFile(State state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(STATE_FILE_PATH))
        )) {
            oos.writeObject(state);
            Log.getInstance().logInfo("State saved to file.");
        } catch (IOException e) {
            Log.getInstance().logError("Error saving state to file: " + e.getMessage());
        }
    }

    /**
     * Deserializes the State object from a file.
     */
    private State loadStateFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(STATE_FILE_PATH)
        )) {
            State state = (State) ois.readObject();
            for (Producer producer : state.getProducerList()) {
                System.out.println(producer);
            }
            Log.getInstance().logInfo("State loaded from file.");
            return state;
        } catch (IOException | ClassNotFoundException e) {
            Log.getInstance().logError("Error loading state from file: " + e.getMessage());
            return null;
        }
    }
}
