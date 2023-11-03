package org.juhnkim.controllers;

import org.juhnkim.models.State;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.StateService;
import org.juhnkim.utils.Log;


public class StateController {

    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    public void saveCurrentState() {
        stateService.saveApplicationState();
    }

    public void loadSavedState() {
        stateService.loadApplicationState();
    }
}
