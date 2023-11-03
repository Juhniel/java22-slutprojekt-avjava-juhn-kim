package org.juhnkim.controllers;

import org.juhnkim.interfaces.LogEventListenerInterface;
import org.juhnkim.services.PropertyChangeService;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import javax.swing.*;

/**
 * MainController orchestrates interactions between the user interface and the underlying services.
 * It implements the LogEventListenerInterface to handle log events and updates the UI accordingly.
 */
public class MainController implements LogEventListenerInterface {
    private final StateController stateController;
    private final ProducerController producerController;
    private final ConsumerController consumerController;
    private final LogController logController;
    private final ProductionRegulatorGUI productionRegulatorGUI;
    private final PropertyChangeService propertyChangeService;

    public MainController(
            StateController stateController,
            ProducerController producerController,
            ConsumerController consumerController,
            PropertyChangeService propertyChangeService,
            ProductionRegulatorGUI productionRegulatorGUI,
            LogController logController) {

        this.logController = logController;
        this.stateController = stateController;
        this.producerController = producerController;
        this.consumerController = consumerController;
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

    /**
     * Initializes the consumers by invoking the consumer controllers initialize method.
     */
    public void initConsumers() {
        consumerController.initialize();
    }

    /**
     * Adds a producer by invoking the producer controller's addProducer method.
     */
    public void addProducer() {
        producerController.addProducer();
    }

    /**
     * Removes a producer by invoking the producer controller's removeProducer method.
     */
    public void removeProducer() {
        producerController.removeProducer();
    }

    /**
     * Saves the current state by invoking the state controller's saveCurrentState method.
     */
    public void saveCurrentState() {
        stateController.saveCurrentState();
    }

    /**
     * Loads a saved state by invoking the state controller's loadSavedState method.
     */
    public void loadSavedState() {
        stateController.loadSavedState();
    }

    /**
     * Logs producer warnings by invoking the log controller's logProducerWarnings method.
     */
    public void logProducerWarnings() {
        logController.logProducerWarnings(propertyChangeService.getBalancePercentage());
    }

    /**
     * Logs the consumed to producer ratio by invoking the log controller's logConsumedToProducerRatio method.
     */
    public void logConsumedToProducerRatio() {
        logController.logConsumedToProducerRatio();
    }

    /**
     * Toggles the auto-adjust feature by invoking the producer controller's toggleAutoAdjust method.
     */
    public void toggleAutoAdjust() {
        producerController.toggleAutoAdjust();
    }

    /**
     * Handles log events by updating the GUI with the log message.
     *
     * @param message the log message received
     */
    @Override
    public void onLogEvent(String message) {
        SwingUtilities.invokeLater(() -> {
            String existingText = productionRegulatorGUI.getTextArea().getText();
            productionRegulatorGUI.getTextArea().setText(message + "\n" + existingText);
            productionRegulatorGUI.getTextArea().setCaretPosition(0);
        });
    }
}
