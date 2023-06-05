package io.github.GreenMushroomer03.todoapp.controller;

import io.github.GreenMushroomer03.todoapp.logic.ProjectService;
import io.github.GreenMushroomer03.todoapp.model.Project;
import io.github.GreenMushroomer03.todoapp.model.projection.write.ProjectWriteModel;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

//    @PostMapping
//    String addProject(
//            @ModelAttribute("project") @Valid ProjectWriteModel current,
//            BindingResult bindingResult,
//            Model model
//    ) {
//        if (bindingResult.hasErrors()) {
//            return "projects";
//        }
//        projectService.save(current);
//
//    }



    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")LocalDateTime deadline
            ) {
    try {
        projectService.createGroup(id, deadline);
        model.addAttribute("message", "Dodano grupę!");
    } catch (IllegalStateException | IllegalArgumentException e) {
        model.addAttribute("message", "Błąd podczas tworzenia grupy!");
    }
    return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return projectService.readAll();
    }
}
