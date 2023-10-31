package org.juhnkim.services;

import org.juhnkim.utils.Log;

import java.util.Random;

public class Consumer implements Runnable {
	private final Buffer buffer;
	private final Random random = new Random();
	boolean isRunning = true;

	public Consumer(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		int consumerInterval = (random.nextInt(10) + 1) * 1000;
		while (isRunning) {
			try {
				Thread.sleep(consumerInterval);
				buffer.remove();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
