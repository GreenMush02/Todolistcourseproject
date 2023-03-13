package io.github.GreenMushroomer03.todoapp.logic;

import io.github.GreenMushroomer03.todoapp.TaskConfigurationProperties;
import io.github.GreenMushroomer03.todoapp.model.*;
import io.github.GreenMushroomer03.todoapp.model.projection.read.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfigurationProperties config;

    public ProjectService(final TaskConfigurationProperties config, final TaskGroupRepository taskGroupRepository, final ProjectRepository projectRepository) {
        this.config = config;
        this.taskGroupRepository = taskGroupRepository;
        this.projectRepository = projectRepository;
    }

    public Project save(Project toSave) {
        return projectRepository.save(toSave);
    }

    public List<Project> readAll() {
        return projectRepository.findAll();
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if(!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream()
                            .map(projectStep -> new Task(
                                    projectStep.getDescription(),
                                    deadline.plusDays(projectStep.getDaysToDeadline()))
                            ).collect(Collectors.toSet())
                );
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup);
    }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }



}
