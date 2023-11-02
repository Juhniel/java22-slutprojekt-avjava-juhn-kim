package org.juhnkim.services;

import org.juhnkim.models.Consumer;
import org.juhnkim.models.Message;
import org.juhnkim.models.Producer;
import org.juhnkim.models.State;
import org.juhnkim.utils.Log;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class StateService {

    private static final String STATE_FILE_PATH = "files/state.dat";

    // Save the current application state to a file
    public void saveApplicationState(List<Producer> producers, List<Consumer> consumers, List<Message> messages) {
        State state = createState(producers, consumers, messages);
        saveStateToFile(state);
    }

    // Load the application state from a file and return it
    public State loadApplicationState() {
        return loadStateFromFile();
    }

    // Helper method to create a State object
    private State createState(List<Producer> producers, List<Consumer> consumers, List<Message> messages) {
        State state = new State();
        state.setProducerList(new LinkedList<>(producers));
        state.setConsumerList(new LinkedList<>(consumers));
        state.setMessageList(new LinkedList<>(messages));
        return state;
    }

    // Helper method to clear State object
    private void clearCurrentState(State state, Buffer buffer) {
//        for(ProducerThread pt : ) {
//
//        }
        state.getProducerList().clear();
        state.getConsumerList().clear();
        state.setMessageList(new LinkedList<>());
        buffer.getAllMessagesInBuffer().clear(); // Assuming Buffer class has a method to clear messages

        Log.getInstance().logInfo("Current state cleared successfully.");
    }


    // Serialize the State object to a file
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

    // Deserialize the State object from a file
    private State loadStateFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(STATE_FILE_PATH)
        )) {
            State state = (State) ois.readObject();
            Log.getInstance().logInfo("State loaded from file.");
            return state;
        } catch (IOException | ClassNotFoundException e) {
            Log.getInstance().logError("Error loading state from file: " + e.getMessage());
            return null;
        }
    }
}
