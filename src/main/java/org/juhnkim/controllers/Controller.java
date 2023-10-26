package org.juhnkim.controllers;

import org.apache.logging.log4j.core.Logger;
import org.juhnkim.models.Message;
import org.juhnkim.services.Buffer;
import org.juhnkim.services.Consumer;
import org.juhnkim.services.Producer;
import org.juhnkim.utils.Log;
import org.juhnkim.views.ProductionRegulatorGUI;

import java.util.LinkedList;

public class Controller {
	private final Logger logger = Log.getInstance().getLogger();
	private final LinkedList<Producer> producerLinkedList;
	private final LinkedList<Consumer> consumersLinkedList;
	private final Buffer buffer;
	private Producer producer;
	private final Message message;
	private final ProductionRegulatorGUI productionRegulatorGUI;
	public Controller(Message message, ProductionRegulatorGUI productionRegulatorGUI, Buffer buffer) {
		this.message = message;
		this.productionRegulatorGUI = productionRegulatorGUI;
		this.buffer = buffer;
		this.producerLinkedList = new LinkedList<>();
		this.consumersLinkedList = new LinkedList<>();
		initController();
		initConsumers();
		new javax.swing.Timer(2000, e -> updateProgressBar()).start();
	}

	private void initController() {
		productionRegulatorGUI.getAddButton().addActionListener(e -> addProducer());
		productionRegulatorGUI.getRemoveButton().addActionListener(e -> removeProducer());
		productionRegulatorGUI.getSaveButton().addActionListener(e -> saveCurrentState());
		productionRegulatorGUI.getLoadButton().addActionListener(e -> loadSavedState());
	}

	/**
	 * Initialize 3-15 consumer threads when the application starts
	 */
	private void initConsumers() {
		int numConsumers = (int) (Math.random() * 15) + 3;
		for (int i = 0; i < numConsumers; i++) {
			Consumer consumer = new Consumer(buffer);
			consumersLinkedList.add(consumer);
			new Thread(consumer).start();
		}
		System.out.println("Consumers: " + consumersLinkedList.size());
	}

	/**
	 * Adding a new Producer Thread
	 */
	private void addProducer() {
		producer = new Producer(buffer);
		producerLinkedList.add(producer);
		new Thread(producer).start();
		logger.info("Producer Added");
		logger.info(producerLinkedList.size());
	}

	/**
	 * Removing a Producer Thread
	 */
	private void removeProducer() {
		if (!producerLinkedList.isEmpty()) {
			producerLinkedList.removeLast().stop();
			logger.info("Producer Removed");
			logger.info(producerLinkedList.size());
		}
	}

	private void saveCurrentState() {
		// Save current state
	}

	private void loadSavedState() {
		// Load the state
	}

	/**
	 * Calculate the amount of messages in queue with a capacity of 100
	 */
	private void updateProgressBar() {
		double balancePercentage = ((double) buffer.getMessageCount() / buffer.getCapacity()*100);
		productionRegulatorGUI.updateProgressBar(balancePercentage);
	}

	private void averageMessages() {
		// Summera antal meddelanden för varje sekund
		// dela med 10 sekunder för att få genomsnittet hur många meddelanden som har producerats
	}

}
