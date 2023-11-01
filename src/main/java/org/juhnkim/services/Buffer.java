package org.juhnkim.services;

import org.juhnkim.models.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

public class Buffer implements Serializable {

	private final Queue<Message> queue;
	private final int capacity;
	private final PropertyChangeSupport support;
	private int producedMessages = 0;
	private int consumedMessages = 0;
	
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
		producedMessages++;
		notify();
		int newQueueSize = queue.size();
		support.firePropertyChange("messageCount", queueSize, newQueueSize);
		System.out.println("Produced: " + queue);
	}

	public synchronized void setAllMessagesInBuffer(List<Message> messages) {
		if(!this.queue.isEmpty()){
			this.queue.clear();
			this.queue.addAll(messages);
		}

	}
	
	public synchronized Message remove() {
		while (queue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return null;
			}
		}
		int queueSize = queue.size();
		Message message = queue.remove();
		consumedMessages++;
		int newQueueSize = queue.size();
		support.firePropertyChange("messageCount", queueSize, newQueueSize);
		return message;
	}

	public synchronized void clear() {
		int oldQueueSize = queue.size();
		queue.clear();
		support.firePropertyChange("messageCount", oldQueueSize, 0);
		System.out.println("Buffer cleared.");
	}

	public synchronized LinkedList<Message> getAllMessagesInBuffer() {
		return new LinkedList<>(queue);
	}



	public int getMessageCount() {
		return queue.size();
	}

	public int getCapacity() {
		return capacity;
	}

	public int getProducedMessages() {
		return producedMessages;
	}

	public int getConsumedMessages() {
		return consumedMessages;
	}

	public void setProducedMessages(int producedMessages) {
		this.producedMessages = producedMessages;
	}

	public void setConsumedMessages(int consumedMessages) {
		this.consumedMessages = consumedMessages;
	}
}
