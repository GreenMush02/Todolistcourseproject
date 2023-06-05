package io.github.GreenMushroomer03.todoapp.controller;

import io.github.GreenMushroomer03.todoapp.model.Task;
import io.github.GreenMushroomer03.todoapp.model.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final ApplicationEventPublisher applicationEventPublisher;

    private final TaskRepository repository;

//    private final TaskService taskService;

    TaskController(ApplicationEventPublisher applicationEventPublisher,  TaskRepository repository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.repository = repository;
    }


    @GetMapping("/test")
    public String getRepositoryString () {
        return repository.toString();
    }
    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){

        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);

    }


    @GetMapping(params={"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
//        return taskService.findAllAsync().thenApply(ResponseEntity::ok);
        return ResponseEntity.ok(repository.findAll());
    }


    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }


    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        logger.info("Exposing task " + id);
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping(value = "/search/done", produces = MediaType.TEXT_XML_VALUE)
//    String foo(){return "";}

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true", required = false) boolean state) {
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

    @GetMapping("/search/today")
    ResponseEntity<List<Task>> readTasksForToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayToCompareAtStart = today.atStartOfDay();
        LocalDateTime todayToCompareAtEnd = today.atTime(LocalTime.MAX);
        return ResponseEntity.ok(repository.findTasksWithDeadlineBetween(todayToCompareAtStart, todayToCompareAtEnd));
    }
    @GetMapping("/test")
    void oldFashionedWay(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println(req.getParameter("foo"));
        resp.getWriter().println("test old-fashioned way");
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });
        return ResponseEntity.noContent().build();
    }
    //Transactional jest adnotacją pochodną od programowania aspektowego, oznacza ona, że przed
    // jej wykonaniem rozpoczyna się transakcja bazodanowa i po, której ta transakcja jest zatwierdzana na bazie.

    @Transactional
    @PatchMapping ("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        repository.findById(id)
                .map(Task::toggle)
                .ifPresent(applicationEventPublisher::publishEvent);
        return ResponseEntity.noContent().build();
    }

}
