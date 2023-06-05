package io.github.GreenMushroomer03.todoapp.model.event;

import io.github.GreenMushroomer03.todoapp.model.Task;

import java.time.Clock;

public class TaskUndone extends TaskEvent {
    public TaskUndone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
