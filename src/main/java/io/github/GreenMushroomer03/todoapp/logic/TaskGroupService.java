package io.github.GreenMushroomer03.todoapp.logic;

import io.github.GreenMushroomer03.todoapp.model.TaskGroup;
import io.github.GreenMushroomer03.todoapp.model.TaskGroupRepository;
import io.github.GreenMushroomer03.todoapp.model.TaskRepository;
import io.github.GreenMushroomer03.todoapp.model.projection.read.GroupReadModel;
import io.github.GreenMushroomer03.todoapp.model.projection.write.GroupWriteModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
//Używamy go aby w obrębie 1 rządania mieć unikalną instancję serwisu. Dopiero gdy wpada rządanie http, to powstaje serwis.
@RequestScope
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;


    public TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll()
                .stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalStateException("group has undone tasks. Done all the tasks first");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }

}
