package org.juhnkim;

import org.juhnkim.controllers.*;
import org.juhnkim.services.*;
import org.juhnkim.views.ProductionRegulatorGUI;

public class Main {
    public static void main(String[] args) {
        // Initialize Buffer and the UI
        Buffer buffer = new Buffer(100);
        ProductionRegulatorGUI productionRegulatorGUI = new ProductionRegulatorGUI();
        productionRegulatorGUI.initializeUI();

        ProducerService producerService = new ProducerService();
        ConsumerService consumerService = new ConsumerService();
        StateService stateService = new StateService();


        // Initialize Controllers
        ProducerController producerController = new ProducerController(productionRegulatorGUI, producerService);
        ConsumerController consumerController = new ConsumerController(buffer, state);
        StateController stateController = new StateController(buffer);
        LogController logController = new LogController(buffer);


        PropertyChangeService propertyChangeService = new PropertyChangeService(productionRegulatorGUI, buffer);
        new Controller(stateController, producerController, consumerController, propertyChangeService, productionRegulatorGUI, logController);
        productionRegulatorGUI.getFrame().setVisible(true);
    }
}
