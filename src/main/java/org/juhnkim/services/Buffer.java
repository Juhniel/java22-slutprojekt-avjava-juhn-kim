package org.juhnkim.services;

import org.juhnkim.models.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;


/**
 * The Buffer class is responsible for storing messages in a queue.
 * It provides synchronized methods to add and remove messages from the queue,
 * as well as to access and manage the queue's properties.
 * It supports property change listeners to notify other components
 * of changes in the message count within the buffer.
 */
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

    /**
     * Registers a property change listener to this buffer.
     *
     * @param pcl the PropertyChangeListener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Adds a message to the buffer and notifies any listeners about the change in message count.
     *
     * @param message the message to add to the buffer
     */
    public synchronized void add(Message message) {
        int queueSize = queue.size();
        queue.add(message);
        producedMessages++;
        notify();
        int newQueueSize = queue.size();
        support.firePropertyChange("messageCount", queueSize, newQueueSize);
    }

    /**
     * Replaces all messages in the buffer with a list of new messages.
     *
     * @param messages the new list of messages to set in the buffer
     */
    public synchronized void setAllMessagesInBuffer(List<Message> messages) {
        if (!this.queue.isEmpty()) {
            this.queue.clear();
        }
        this.queue.addAll(messages);
    }

    /**
     * Removes and returns the head of the queue, waiting if necessary until an element becomes available.
     */
    public synchronized void remove() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        int queueSize = queue.size();
        queue.remove();
        consumedMessages++;
        int newQueueSize = queue.size();
        support.firePropertyChange("messageCount", queueSize, newQueueSize);
    }

    /**
     * Clears the buffer and notifies listeners that the buffer is empty.
     */
    public synchronized void clear() {
        int oldQueueSize = queue.size();
        queue.clear();
        support.firePropertyChange("messageCount", oldQueueSize, 0);
    }

    /**
     * Retrieves all messages in the buffer as a linked list.
     *
     * @return a linked list containing all the messages in the buffer
     */
    public synchronized LinkedList<Message> getAllMessagesInBuffer() {
        return new LinkedList<>(queue);
    }

    /**
     * Calculates the ratio of consumed messages to produced messages.
     *
     * @return the consumed ratio as a percentage
     */
    public double consumedRatio() {
        return ((double) getConsumedMessages() / getProducedMessages()) * 100;
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
