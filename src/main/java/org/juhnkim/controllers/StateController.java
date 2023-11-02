package org.juhnkim.controllers;

import org.juhnkim.models.State;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.StateService;
import org.juhnkim.utils.Log;


public class StateController {
    private final Buffer buffer;
    private State state;
    private final StateService stateService;

    public StateController(Buffer buffer, State state, StateService stateService) {
        this.buffer = buffer;
        this.state = state;
        this.stateService = stateService;
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
                state = loadedState;
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
}
