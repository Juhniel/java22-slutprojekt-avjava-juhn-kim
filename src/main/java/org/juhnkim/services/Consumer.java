package org.juhnkim.services;
import java.io.Serializable;
import java.util.Random;

public class Consumer implements Runnable, Serializable {
	private final Buffer buffer;
	private final Random random = new Random();
	public int consumerInterval;
	boolean isRunning = true;

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
		consumerInterval = (random.nextInt(10) + 1) * 1000;
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
