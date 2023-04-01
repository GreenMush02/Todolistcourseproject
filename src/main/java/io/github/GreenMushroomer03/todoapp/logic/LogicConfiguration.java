package io.github.GreenMushroomer03.todoapp.logic;

import io.github.GreenMushroomer03.todoapp.TaskConfigurationProperties;
import io.github.GreenMushroomer03.todoapp.model.ProjectRepository;
import io.github.GreenMushroomer03.todoapp.model.TaskGroupRepository;
import io.github.GreenMushroomer03.todoapp.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//Jeżeli chcemy przestać polegać na springu, a jedynie na czystych klasach javowych to wywalamy tą konfiguracje.
@Configuration
public class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskGroupService service,
            final TaskConfigurationProperties config
            ) {
        return new ProjectService(taskGroupRepository, repository, service, config);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository taskGroupRepository,
            final TaskRepository taskRepository
            ) {
        return new TaskGroupService(taskGroupRepository, taskRepository);
    }
}
