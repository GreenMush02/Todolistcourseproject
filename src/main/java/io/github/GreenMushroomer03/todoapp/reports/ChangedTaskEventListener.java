package io.github.GreenMushroomer03.todoapp.reports;

import io.github.GreenMushroomer03.todoapp.model.event.TaskDone;
import io.github.GreenMushroomer03.todoapp.model.event.TaskEvent;
import io.github.GreenMushroomer03.todoapp.model.event.TaskUndone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
class ChangedTaskEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ChangedTaskEventListener.class);

    private final PersistedTaskEventRepository repository;

    ChangedTaskEventListener(PersistedTaskEventRepository repository) {
        this.repository = repository;
    }

    @Async
    @EventListener
    public void on(final TaskDone event) {
        onChanged(event);
    }

    @Async
    @EventListener
    public void on(final TaskUndone event) {
        onChanged(event);
    }

    public void onChanged(final TaskEvent event) {
        logger.info("Got " + event);
        repository.save(new PersistedTaskEvent(event));
    }
}
