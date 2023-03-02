package io.github.GreenMushroomer03.todoapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "task_groups")
public class TaskGroup extends TaskBased{

    //cascade ALL -> whenever group is removed, changed or something -> all tasks in are also...
    //mappedBy -> points a field that owns the relation (field with @ManyToOne addnot)
    //fetch LAZY -> lazy loading -> tasks are loaded in sql QUERY only when it is necessary
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group", fetch = FetchType.LAZY)
    private Set<Task> tasks;
    @Embedded
    private Audit audit = new Audit();


    public TaskGroup(){

    }
    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void updateFrom(TaskGroup source) {
        super.updateFrom(source);
        tasks = source.tasks;
    }
}