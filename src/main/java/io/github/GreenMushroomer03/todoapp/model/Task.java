package io.github.GreenMushroomer03.todoapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Task's description must be not empty")
    private String description;
    private boolean done;
    @Column()
    private LocalDateTime deadline;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    public Task(){

    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void updateFrom(final Task source) {
        description = source.description;
        done = source.done;
        deadline = source.deadline;
    }
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    @PrePersist
    void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preMerge() {
        updatedOn = LocalDateTime.now();
    }
}