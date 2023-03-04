package io.github.GreenMushroomer03.todoapp.adapter;

import io.github.GreenMushroomer03.todoapp.model.TaskGroup;
import io.github.GreenMushroomer03.todoapp.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {
//We are using HQL, entity in QUERY in default option is className
    //distinct ratuje nas przed ewentualnym zwróceniem jednej grupy tyle razy ile posiadałaby tasków
    @Override
    @Query("select distinct g from TaskGroup g join fetch g.tasks ")
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
