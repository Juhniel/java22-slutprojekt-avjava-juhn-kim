package org.juhnkim.models;

import java.io.Serializable;
import java.util.Random;

public class Producer implements Serializable {

	private int producerInterval;

	public Producer() {
		Random random = new Random();
		this.producerInterval = (random.nextInt(10) + 1) * 1000;
	}

	public int getProducerInterval() {
		return producerInterval;
	}

	public void setProducerInterval(int producerInterval) {
		this.producerInterval = producerInterval;
	}

	@Override
	public String toString() {
		return "Producer{" +
				"producerInterval=" + producerInterval +
				// Include other properties here
				'}';
	}
}










