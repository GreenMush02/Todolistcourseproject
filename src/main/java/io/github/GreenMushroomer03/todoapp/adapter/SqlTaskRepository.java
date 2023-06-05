package io.github.GreenMushroomer03.todoapp.adapter;

import io.github.GreenMushroomer03.todoapp.model.Task;
import io.github.GreenMushroomer03.todoapp.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer id);

    //We can create queries writing it like in english language. Of course we need Idea Ultimate to get hints :)
    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    @Query("SELECT t FROM Task t WHERE (t.deadline IS NULL OR t.deadline >= :date1) AND (t.deadline IS NULL OR t.deadline <= :date2)")
    List<Task> findTasksWithDeadlineBetween(@Param("date1") LocalDateTime date1, @Param("date2") LocalDateTime date2);


}
