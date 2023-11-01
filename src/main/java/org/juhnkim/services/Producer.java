package org.juhnkim.services;

import org.apache.logging.log4j.Logger;
import org.juhnkim.models.Message;
import org.juhnkim.utils.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class Producer implements Runnable, Serializable {
	private final Buffer buffer;
	public int producerInterval;
	volatile boolean isRunning = true;
	private Random random = new Random();



	public Producer(Buffer buffer) {
		this.buffer = buffer;
	}

	public int getProducerInterval() {
		return producerInterval;
	}

	public void setProducerInterval(int producerInterval) {
		this.producerInterval = producerInterval;
	}



	public void stop() {
		isRunning = false;
	}

	@Override
	public void run() {
		String text = "Random text";
		producerInterval = (random.nextInt(10) + 1) * 1000;
		Log.getInstance().logInfo("Producer will sleep for " + producerInterval +" milliseconds before producing the next message");
		while (isRunning) {
			try {
				Thread.sleep(producerInterval);
				buffer.add(new Message(text, LocalDate.now(), LocalTime.now()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



}










