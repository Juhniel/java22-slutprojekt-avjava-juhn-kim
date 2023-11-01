package org.juhnkim;

import org.juhnkim.controllers.*;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.PropertyChangeService;
import org.juhnkim.views.ProductionRegulatorGUI;

public class Main {
    public static void main(String[] args) {
        // Initialize Buffer and the UI
        Buffer buffer = new Buffer(100);
        ProductionRegulatorGUI productionRegulatorGUI = new ProductionRegulatorGUI();
        productionRegulatorGUI.initializeUI();

        // Initialize Controllers
        ThreadController threadController = new ThreadController(productionRegulatorGUI, buffer);
        StateController stateController = new StateController(buffer);
        LogController logController = new LogController(buffer);


        PropertyChangeService propertyChangeService = new PropertyChangeService(productionRegulatorGUI, buffer);
        new Controller(stateController, threadController, propertyChangeService, productionRegulatorGUI, logController);
        productionRegulatorGUI.getFrame().setVisible(true);
    }
}
