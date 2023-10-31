package org.juhnkim.services;

import org.juhnkim.models.State;
import org.juhnkim.utils.Log;

import java.io.*;

public class StateService {

    public void saveState(State state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream("files/state.dat"))
        )) {
            oos.writeObject(state);
            Log.getInstance().logInfo("State saved");
        } catch (IOException e) {
            Log.getInstance().logInfo("Error saving state" + e);
        }
    }

    public State loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/state.dat"))) {
            State state = (State) ois.readObject();
            Log.getInstance().logInfo("State loaded");
            return state;
        } catch (IOException | ClassNotFoundException e) {
            Log.getInstance().logInfo("Error loading state" + e);
            return null;
        }
    }
}
