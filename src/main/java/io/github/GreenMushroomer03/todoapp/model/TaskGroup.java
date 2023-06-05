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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    private boolean done;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group", fetch = FetchType.LAZY)
    private Set<Task> tasks;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;


    public TaskGroup(){

    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void updateFrom(TaskGroup source) {
        done = source.done;
        tasks = source.tasks;
    }
}