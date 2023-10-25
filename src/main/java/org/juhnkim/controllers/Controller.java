package main;

public class Controller {

	public static void main(String[] args) {
		Buffer buffer = new Buffer();
		
		Producer producer = new Producer(buffer);
		Thread producerThread = new Thread(producer);
		producerThread.start();
		
		Consumer consumer = new Consumer(buffer);
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();
		
	}
}
