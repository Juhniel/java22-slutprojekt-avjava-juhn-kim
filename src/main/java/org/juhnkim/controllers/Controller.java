package org.juhnkim.controllers;

import org.juhnkim.models.Message;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.Producer;
import org.juhnkim.views.ProductionRegulatorGUI;

public class Controller {
	private Producer producer;
	private final Message message;
	private final ProductionRegulatorGUI productionRegulatorGUI;
	public Controller(Message message, ProductionRegulatorGUI productionRegulatorGUI) {
		this.message = message;
		this.productionRegulatorGUI = productionRegulatorGUI;
	}

	private void initController() {
		productionRegulatorGUI.getAddButton().addActionListener(e -> addProducer());
		productionRegulatorGUI.getRemoveButton().addActionListener(e -> removeProducer());
		productionRegulatorGUI.getSaveButton().addActionListener(e -> saveCurrentState());
		productionRegulatorGUI.getLoadButton().addActionListener(e -> loadCurrentState());
	}

	private void addProducer() {
		producer = new Producer(new Buffer());
		producer.run();
	}

	private void removeProducer() {
		Thread.currentThread().interrupt();
	}






}
