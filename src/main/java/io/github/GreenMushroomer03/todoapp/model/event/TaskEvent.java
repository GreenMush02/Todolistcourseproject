package io.github.GreenMushroomer03.todoapp.model.event;

import io.github.GreenMushroomer03.todoapp.model.Task;

import java.time.Clock;
import java.time.Instant;

public abstract class TaskEvent {
    public static TaskEvent changed(Task source) {
       return source.isDone() ? new TaskDone(source) : new TaskUndone(source);
    };
    private int taskId;
    private Instant occurence;

    public Instant getOccurence() {
        return occurence;
    }
    public int getTaskId() {
        return taskId;
    }

    TaskEvent(final int taskId, Clock clock) {
        this.taskId = taskId;
        this.occurence = Instant.now(clock);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", occurence=" + occurence +
                '}';
    }
}
