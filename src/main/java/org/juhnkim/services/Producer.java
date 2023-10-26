package org.juhnkim.services;

import org.juhnkim.models.Message;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

public class Producer implements Runnable {
	private final Buffer buffer;
	private final Random random = new Random();
	volatile boolean isRunning = true;

	public Producer(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		String text = "Random text";
		while (isRunning) {
			try {
				Thread.sleep((random.nextInt(10) + 1) * 1000);
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










