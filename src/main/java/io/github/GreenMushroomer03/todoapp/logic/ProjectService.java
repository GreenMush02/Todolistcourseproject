package io.github.GreenMushroomer03.todoapp.logic;

import io.github.GreenMushroomer03.todoapp.TaskConfigurationProperties;
import io.github.GreenMushroomer03.todoapp.model.*;
import io.github.GreenMushroomer03.todoapp.model.projection.read.GroupReadModel;
import io.github.GreenMushroomer03.todoapp.model.projection.write.GroupTaskWriteModel;
import io.github.GreenMushroomer03.todoapp.model.projection.write.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private TaskGroupRepository taskGroupRepository;
    private TaskGroupService service;
    private TaskConfigurationProperties config;


    public ProjectService(final TaskGroupRepository taskGroupRepository, final ProjectRepository projectRepository, final TaskGroupService service, final TaskConfigurationProperties config) {
        this.config = config;
        this.taskGroupRepository = taskGroupRepository;
        this.projectRepository = projectRepository;
        this.service = service;
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
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                        var task = new GroupTaskWriteModel();
                                        task.setDescription(projectStep.getDescription());
                                        task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                        return task;
                                            }
                                    ).collect(Collectors.toSet())
                    );
                    return service.createGroup(targetGroup);
    }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
