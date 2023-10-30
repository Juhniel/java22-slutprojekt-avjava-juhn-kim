package org.juhnkim.services;

import java.util.Random;

public class Consumer implements Runnable {
	private final Buffer buffer;
	private final Random random = new Random();
	boolean isRunning = true;
	private int consumerInterval;

	public Consumer(Buffer buffer) {
		this.buffer = buffer;
	}

	public int getConsumerInterval() {
		return consumerInterval;
	}

	public void setConsumerInterval(int consumerInterval) {
		this.consumerInterval = consumerInterval;
	}

	@Override
	public void run() {
		consumerInterval = (random.nextInt(10) + 1) * 1000;  // Capture the interval time
		while (isRunning) {
			try {
				Thread.sleep(consumerInterval);
				buffer.remove();
//				System.out.println("Consumed: " + buffer.remove());
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}

}
