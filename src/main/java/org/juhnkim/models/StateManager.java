package org.juhnkim.models;

import org.apache.logging.log4j.Logger;
import org.juhnkim.utils.Log;

import java.io.*;

public class StateManager {
    private final Logger logger = Log.getInstance().getLogger();

    public void saveState(State state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("state.dat"))) {
            oos.writeObject(state);
            logger.info("State saved");
        } catch (IOException e) {
            logger.error("Error saving state", e);
        }
    }

    public State loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("state.dat"))) {
            State state = (State) ois.readObject();
            logger.info("State loaded");
            return state;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error loading state", e);
            return null;
        }
    }
}
