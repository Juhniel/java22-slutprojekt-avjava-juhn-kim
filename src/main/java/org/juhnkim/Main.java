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
        Buffer buffer = new Buffer(100);

        Message message = new Message();

        ProductionRegulatorGUI productionRegulatorGUI = new ProductionRegulatorGUI();

        productionRegulatorGUI.initializeUI();

        Controller controller = new Controller(message, productionRegulatorGUI, buffer);

        productionRegulatorGUI.getFrame().setVisible(true);

    }
}
