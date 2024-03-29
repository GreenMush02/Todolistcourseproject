package io.github.GreenMushroomer03.todoapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.GreenMushroomer03.todoapp.model.event.TaskEvent;
import io.github.GreenMushroomer03.todoapp.model.event.TaskUndone;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


@Entity
@Table(name = "tasks")
public class Task extends TaskBased {

    @JsonProperty("done")
    private boolean done;
    @JsonProperty("deadline")
    private LocalDateTime deadline;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;
    @Embedded
    private Audit audit = new Audit();


    public Task(){

    }

    public Task(String description, LocalDateTime deadline){
       this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup group){
        this.description = description;
        this.deadline = deadline;
        if (group != null) {
            this.group = group;
        }
    }


    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void updateFrom(Task source) {
        super.updateFrom(source);
        done = source.done;
        deadline = source.deadline;
        group = source.group;
    }
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return done;
    }

    public TaskEvent toggle() {
        this.done = !this.done;
        return TaskEvent.changed(this);
    }

    public TaskGroup getGroup() {
        return group;
    }
}