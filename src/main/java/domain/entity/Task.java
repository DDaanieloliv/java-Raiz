package main.java.domain.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class Task {

    private String id;

    private String description;

    private LocalDateTime datetimeFrame;

    private LocalDateTime currentDatePosting;




    public Task(String description, LocalDateTime datetimeFrame) {
        this.id = UUID.randomUUID().toString(); // Gerando um UUID como ID
        this.currentDatePosting = LocalDateTime.now();
        this.description = description;
        this.datetimeFrame = datetimeFrame;
    }

    public Task(String id, String description, Timestamp datetimeFrame, Timestamp currentDatePosting) {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDatetimeFrame() {
        return datetimeFrame;
    }

    public void setDatetimeFrame(LocalDateTime datetimeFrame) {
        this.datetimeFrame = datetimeFrame;
    }

    public LocalDateTime getCurrentDatePosting() {
        return currentDatePosting;
    }

    public void setCurrentDatePosting(LocalDateTime currentDatePosting) {
        this.currentDatePosting = currentDatePosting;
    }
}
