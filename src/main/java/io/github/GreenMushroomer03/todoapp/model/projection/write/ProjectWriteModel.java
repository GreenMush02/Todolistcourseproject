package io.github.GreenMushroomer03.todoapp.model.projection.write;

import io.github.GreenMushroomer03.todoapp.model.Project;
import io.github.GreenMushroomer03.todoapp.model.ProjectStep;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {
    @NotBlank(message = "Description must not be empty")
    private String description;
    // lista kroków powinna przechodzić wszystkie potrzebne walidacje (model dedykowany pod ekrany)
    @Valid
    private List<ProjectStep> steps;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject() {
        var result = new Project();
        steps.forEach(step -> step.setProject(result));
        result.setSteps(new HashSet<>(steps));
        return result;
    }
}
