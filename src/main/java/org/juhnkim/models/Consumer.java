package org.juhnkim.models;

import java.io.Serializable;
import java.util.Random;

public class Consumer implements Serializable {

	private int consumerInterval;

	public Consumer() {
		Random random = new Random();
		this.consumerInterval = (random.nextInt(10) + 1) * 1000;
	}

	public int getConsumerInterval() {
		return consumerInterval;
	}

	public void setConsumerInterval(int consumerInterval) {
		this.consumerInterval = consumerInterval;
	}
}
