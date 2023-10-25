package org.juhnkim.controllers;

import org.juhnkim.models.Message;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.Producer;
import org.juhnkim.views.ProductionRegulatorGUI;

public class Controller {

	private final Buffer buffer;
	private Producer producer;
	private final Message message;
	private final ProductionRegulatorGUI productionRegulatorGUI;
	public Controller(Message message, ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer) {
		this.message = message;
		this.productionRegulatorGUI = productionRegulatorGUI;
		this.buffer = buffer;
		initController();
		new javax.swing.Timer(1000, e -> updateProgressBar()).start();
	}

	private void initController() {
		productionRegulatorGUI.getAddButton().addActionListener(e -> addProducer());
		productionRegulatorGUI.getRemoveButton().addActionListener(e -> removeProducer());
		productionRegulatorGUI.getSaveButton().addActionListener(e -> saveCurrentState());
		productionRegulatorGUI.getLoadButton().addActionListener(e -> loadSavedState());
	}

	private void addProducer() {
		producer = new Producer(buffer);
		new Thread(producer).start();
	}

	private void removeProducer() {
		if(producer != null) {
			producer.stop();
		}
	}

	private void saveCurrentState() {
		// Save current state
	}

	private void loadSavedState() {
		// Load the state
	}

	private void updateProgressBar() {
		int balancePercentage = (buffer.getMessageCount() / buffer.getCapacity()) * 100;

		// Update the GUI
		productionRegulatorGUI.updateProgressBar(balancePercentage);
	}

}
