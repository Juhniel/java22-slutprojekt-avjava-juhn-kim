package main;

public class Producer implements Runnable {
	Buffer buffer = null;
	boolean isRunning = true;

	public Producer(Buffer buffer) {
		this.buffer = buffer;
	}
	
	
	@Override
	public void run() {

		while (isRunning) {
			try {
				Thread.sleep(2000);
				
				buffer.add(new Item(""+(char)((int)(Math.random()*100))));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}










