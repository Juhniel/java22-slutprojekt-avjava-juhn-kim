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


        // Initialize Services
        ProducerService producerService = new ProducerService(productionRegulatorGUI, buffer, state);
        ConsumerService consumerService = new ConsumerService(buffer, state);
        StateService stateService = new StateService(producerService, consumerService, state, buffer);
        PropertyChangeService propertyChangeService = new PropertyChangeService(productionRegulatorGUI, buffer);

        // Initialize Controllers
        ProducerController producerController = new ProducerController(producerService);
        ConsumerController consumerController = new ConsumerController(consumerService);
        StateController stateController = new StateController(stateService);
        LogController logController = new LogController(buffer);


        new MainController(stateController, producerController, consumerController, propertyChangeService, productionRegulatorGUI, logController);
        productionRegulatorGUI.getFrame().setVisible(true);

    }
}
