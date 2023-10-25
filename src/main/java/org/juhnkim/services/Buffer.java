package org.juhnkim.services;

import org.juhnkim.models.Message;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {

	Queue<Message> buffer = new LinkedList<>();
	
	
	public synchronized void add(Message message) {
		buffer.add(message);
		notify();
		System.out.println(buffer);
	}
	
	public synchronized Message remove() {
		if(buffer.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return buffer.remove();
	}
	
	
}
