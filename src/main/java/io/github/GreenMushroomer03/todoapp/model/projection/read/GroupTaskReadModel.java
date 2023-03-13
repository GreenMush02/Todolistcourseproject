package io.github.GreenMushroomer03.todoapp.model.projection.read;

import io.github.GreenMushroomer03.todoapp.model.Task;

public class GroupTaskReadModel {
    private String description;
    private boolean done;

    public GroupTaskReadModel(Task source) {
        description = source.description;
        done = source.isDone();

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
}
