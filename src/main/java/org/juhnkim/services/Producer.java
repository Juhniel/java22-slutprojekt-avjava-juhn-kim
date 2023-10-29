package org.juhnkim.services;

import org.apache.logging.log4j.Logger;
import org.juhnkim.models.Message;
import org.juhnkim.utils.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class Producer implements Runnable {
	private final Logger logger = Log.getInstance().getLogger();
	private final Buffer buffer;
	private final Random random = new Random();
	volatile boolean isRunning = true;

	public Producer(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		String text = "Random text";
		int interval = (random.nextInt(10) + 1) * 1000;  // Capture the interval time
		logger.info("Producer will sleep for {} milliseconds before producing the next message", interval); // Log the interval

		while (isRunning) {
			try {
				Thread.sleep(interval);
				buffer.add(new Message(text, LocalDate.now(), LocalTime.now()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		isRunning = false;
	}

}










