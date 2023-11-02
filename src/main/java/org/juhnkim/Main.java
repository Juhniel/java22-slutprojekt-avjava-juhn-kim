package org.juhnkim;

import org.juhnkim.controllers.*;
import org.juhnkim.models.State;
import org.juhnkim.services.*;
import org.juhnkim.views.ProductionRegulatorGUI;

public class Main {
    public static void main(String[] args) {
        // Initialize Buffer and the UI
        Buffer buffer = new Buffer(100);
        State state = new State();
        ProductionRegulatorGUI productionRegulatorGUI = new ProductionRegulatorGUI();
        productionRegulatorGUI.initializeUI();

        ProducerService producerService = new ProducerService(productionRegulatorGUI, buffer, state);
        new ConsumerService(buffer, state);
        new StateService();


        // Initialize Controllers
        ProducerController producerController = new ProducerController(producerService);
        ConsumerController consumerController = new ConsumerController(buffer, state);
        StateController stateController = new StateController(buffer);
        LogController logController = new LogController(buffer);


        PropertyChangeService propertyChangeService = new PropertyChangeService(productionRegulatorGUI, buffer);
        new Controller(stateController, producerController, consumerController, propertyChangeService, productionRegulatorGUI, logController);
        productionRegulatorGUI.getFrame().setVisible(true);
    }
}
