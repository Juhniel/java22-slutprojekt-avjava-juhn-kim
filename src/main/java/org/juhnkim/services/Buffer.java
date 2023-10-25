package org.juhnkim.services;

import org.juhnkim.models.Message;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {

	private final Queue<Message> queue;
	private final int capacity;
	
	public Buffer(int capacity) {
		this.capacity = capacity;
		this.queue = new LinkedList<>();
	}

	public synchronized void add(Message message) {
		queue.add(message);
		notify();
		System.out.println("Produced: " + queue);
	}
	
	public synchronized Message remove() {
		if(queue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return queue.remove();
	}
	
	public int getMessageCount() {
		return queue.size();
	}

	public int getCapacity() {
		return capacity;
	}
}
