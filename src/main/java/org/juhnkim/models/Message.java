package org.juhnkim.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Message {
	private String id;
	private String body;
	private LocalDate localDate;
	private LocalTime localTime;

	public Message() {

	}
	public Message(String body, LocalDate localDate, LocalTime localTime) {
		this.id = String.valueOf(UUID.randomUUID());
		this.body = body;
		this.localDate = localDate;
		this.localTime = localTime;
	}

	@Override
	public String toString() {
		return "Message{" +
				"id='" + id + '\'' +
				", body='" + body + '\'' +
				", localDate=" + localDate +
				", localTime=" + localTime +
				'}';
	}
}
