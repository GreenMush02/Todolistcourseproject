package io.github.GreenMushroomer03.todoapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;


@Entity
@Table(name = "tasks")
public class Task extends TaskBased {

    private LocalDateTime deadline;
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;
    @Embedded
    private Audit audit = new Audit();

    public Task(){

    }

    public Task(String description, LocalDateTime deadline){
        this.description = description;
        this.deadline = deadline;
    }


    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void updateFrom(Task source) {
        super.updateFrom(source);
        deadline = source.deadline;
        group = source.group;
    }
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }


    public TaskGroup getGroup() {
        return group;
    }
}