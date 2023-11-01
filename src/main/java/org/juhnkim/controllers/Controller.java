package org.juhnkim.controllers;

import org.juhnkim.interfaces.LogEventListenerInterface;
import org.juhnkim.services.PropertyChangeService;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import javax.swing.*;

public class Controller implements LogEventListenerInterface {
    private final StateController stateController;
    private final ThreadController threadController;
    private final LogController logController;
    private final ProductionRegulatorGUI productionRegulatorGUI;

    private final PropertyChangeService propertyChangeService;

    public Controller(
            StateController stateController,
            ThreadController threadController,
            PropertyChangeService propertyChangeService,
            ProductionRegulatorGUI productionRegulatorGUI,
            LogController logController) {

        this.logController = logController;
        this.stateController = stateController;
        this.threadController = threadController;
        this.productionRegulatorGUI = productionRegulatorGUI;
        this.propertyChangeService = propertyChangeService;
        Log.getInstance().addLogEventListener(this);
        initController();
        initConsumers();
        new javax.swing.Timer(10000, e -> logConsumedToProducerRatio()).start();
        new javax.swing.Timer(5000, e -> logProducerWarnings()).start();
    }

    /**
     * Initialize buttons on the gui and set ActionListeners to buttons
     */
    private void initController() {
        productionRegulatorGUI.getAddButton().addActionListener(e -> addProducer());
        productionRegulatorGUI.getRemoveButton().addActionListener(e -> removeProducer());
        productionRegulatorGUI.getSaveButton().addActionListener(e -> saveCurrentState());
        productionRegulatorGUI.getLoadButton().addActionListener(e -> loadSavedState());
        productionRegulatorGUI.getAutoAdjustButton().addActionListener(e -> toggleAutoAdjust());
    }

    public void initConsumers() {
        threadController.initConsumers();
    }

    public void addProducer() {
        threadController.addProducer();
    }

    public void removeProducer() {
        threadController.removeProducer();
    }

    public void saveCurrentState() {
        stateController.saveCurrentState();
    }

    public void loadSavedState() {
        stateController.loadSavedState();
    }

    public void logProducerWarnings() {
        logController.logProducerWarnings(propertyChangeService.getBalancePercentage());
    }

    public void logConsumedToProducerRatio() {
        logController.logConsumedToProducerRatio();
    }

    public void toggleAutoAdjust() {
        threadController.toggleAutoAdjust();
    }

    @Override
    public void onLogEvent(String message) {
        SwingUtilities.invokeLater(() -> {
            String existingText = productionRegulatorGUI.getTextArea().getText();
            productionRegulatorGUI.getTextArea().setText(message + "\n" + existingText);
            productionRegulatorGUI.getTextArea().setCaretPosition(0);
        });
    }
}
