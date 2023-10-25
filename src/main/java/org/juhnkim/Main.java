package org.juhnkim;

import org.juhnkim.controllers.Controller;
import org.juhnkim.models.Message;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.Consumer;
import org.juhnkim.views.ProductionRegulatorGUI;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize shared resources, like a Buffer
        Buffer buffer = new Buffer(5);

        // Initialize models, like a Message (if needed)
        Message message = new Message();

        // Initialize consumers
        List<Consumer> consumers = new ArrayList<>();
        int numConsumers = (int) (Math.random() * 15) + 3; // Random number between 3 and 15
        for (int i = 0; i < numConsumers; i++) {
            Consumer consumer = new Consumer(buffer);
            consumers.add(consumer);
            new Thread(consumer).start();
        }

        // Initialize the view (GUI)
        ProductionRegulatorGUI productionRegulatorGUI = new ProductionRegulatorGUI();

        productionRegulatorGUI.initializeUI();

        // Initialize the controller, linking it to models and views
        Controller controller = new Controller(message, productionRegulatorGUI, buffer);

        // Display the GUI or do any other final setup
        productionRegulatorGUI.getFrame().setVisible(true);

    }
}
