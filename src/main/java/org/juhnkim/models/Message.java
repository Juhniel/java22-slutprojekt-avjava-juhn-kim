package org.juhnkim.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Message implements Serializable {
    private final String id;
    private final String body;
    private final LocalDate localDate;
    private final LocalTime localTime;


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
