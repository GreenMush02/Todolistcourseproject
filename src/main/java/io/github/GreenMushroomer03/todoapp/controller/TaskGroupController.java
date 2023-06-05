package io.github.GreenMushroomer03.todoapp.controller;

import io.github.GreenMushroomer03.todoapp.logic.TaskGroupService;
import io.github.GreenMushroomer03.todoapp.model.TaskRepository;
import io.github.GreenMushroomer03.todoapp.model.projection.read.GroupReadModel;
import io.github.GreenMushroomer03.todoapp.model.projection.write.GroupWriteModel;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@IllegalExceptionsProcessing
@RequestMapping("/groups")
public class TaskGroupController {
    public static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    private final TaskGroupService service;

    private final TaskRepository repository;

    TaskGroupController(final TaskGroupService service, final TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/test")
    public String getRepositoryString () {
        return repository.toString();
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.info("Exposing all groups!");
        List<GroupReadModel> result = service.readAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        GroupReadModel result = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }


    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleGroup(@PathVariable int id){
        service.toggleGroup(id);
        return ResponseEntity.ok().build();
    }


}
