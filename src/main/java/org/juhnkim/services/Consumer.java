package main;

public class Consumer implements Runnable {
	Buffer buffer = null;
	boolean isRunning = true;

	public Consumer(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {

		while (isRunning) {
			try {
				Thread.sleep(2000);
				System.out.println("Consumed: " + buffer.remove());
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

	}

}
