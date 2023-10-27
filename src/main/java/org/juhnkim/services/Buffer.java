package org.juhnkim.services;

import org.juhnkim.models.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class Buffer {

	private final Queue<Message> queue;
	private final int capacity;

	private final PropertyChangeSupport support;
	
	public Buffer(int capacity) {
		this.capacity = capacity;
		this.queue = new LinkedList<>();
		this.support = new PropertyChangeSupport(this);

	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	public synchronized void add(Message message) {
		int queueSize = queue.size();
		queue.add(message);
		notify();
		int newQueueSize = queue.size();
		support.firePropertyChange("messageCount", queueSize, newQueueSize);
		System.out.println("Produced: " + queue);
	}
	
	public synchronized Message remove() {
		while (queue.isEmpty()) { // Note the use of 'while' instead of 'if' to recheck the condition
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Re-interrupt the thread
				return null;
			}
		}
		int queueSize = queue.size();
		Message message = queue.remove();
		int newQueueSize = queue.size();
		support.firePropertyChange("messageCount", queueSize, newQueueSize);
		return message;
	}
	
	public int getMessageCount() {
		return queue.size();
	}

	public int getCapacity() {
		return capacity;
	}
}
