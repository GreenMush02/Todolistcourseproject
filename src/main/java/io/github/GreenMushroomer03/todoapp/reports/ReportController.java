package io.github.GreenMushroomer03.todoapp.reports;

import io.github.GreenMushroomer03.todoapp.model.Task;
import io.github.GreenMushroomer03.todoapp.model.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final TaskRepository taskRepository;
    private final PersistedTaskEventRepository repository;

    public ReportController(TaskRepository taskRepository, PersistedTaskEventRepository repository) {
        this.taskRepository = taskRepository;
        this.repository = repository;
    }

    @GetMapping("/isCompletedBeforeDeadline/{id}")
    ResponseEntity<TaskIsDoneBeforeDeadline> readIfTaskIsDoneBeforeDeadline (@PathVariable int id) {
        return taskRepository.findById(id)
                .map(task -> new TaskIsDoneBeforeDeadline(task, repository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/count/{id}")
    ResponseEntity<TaskWithChangesCount> readTaskWithCount (@PathVariable int id) {
        return taskRepository.findById(id)
                .map(task -> new TaskWithChangesCount(task, repository.findByTaskId(id)))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    private static class TaskWithChangesCount {
        public String description;
        public boolean done;
        public int changesCount;
        TaskWithChangesCount(Task task, List<PersistedTaskEvent> events) {
            description = task.getDescription();
            done = task.isDone();
            changesCount = events.size();
        }
    }

    private static class TaskIsDoneBeforeDeadline {
        public String description;
        public LocalDateTime deadline;
        public boolean done;
        public boolean isDoneBeforeDeadline;

        TaskIsDoneBeforeDeadline(Task task, List<PersistedTaskEvent> events) {
            description = task.getDescription();
            deadline = task.getDeadline();
            done = task.isDone();
            if (events != null && !events.isEmpty() && deadline != null && done) {
                isDoneBeforeDeadline = events.get(events.size() - 1).getOccurrence().isAfter(deadline);
            } else {
                isDoneBeforeDeadline = false;
            }
        }
    }
}
