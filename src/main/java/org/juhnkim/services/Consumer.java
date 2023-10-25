package org.juhnkim.services;

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
		while (isRunning) {
			try {
				Thread.sleep((random.nextInt(10) + 1) * 1000);
				System.out.println("Consumed: " + buffer.remove());
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

	}

}
