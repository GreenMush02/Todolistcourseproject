package io.github.GreenMushroomer03.todoapp.logic;

import io.github.GreenMushroomer03.todoapp.TaskConfigurationProperties;
import io.github.GreenMushroomer03.todoapp.model.*;
import io.github.GreenMushroomer03.todoapp.model.projection.read.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists.")
    void createGroup_noMultipleGroupsConfig_And_openGroups_throwsIllegalStateException() {
        //given (miejsce w, którym przygotowujemy dane)
        //Zaletą lekkiego kontraktu interfejsowego jest to, że normalnie musielibyśmy nadpisać wszystkie metody z SqlTaskGroupRepository, które dodatkowo rozszerza JPA.
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(false);
        //system under test
        var toTest = new ProjectService(mockGroupRepository, null, null, mockConfig);
        // when (gdy wołamy testowaną metodę)
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
        //then (czy metoda wyskutkowała to co założyliśmy)
//        assertThatExceptionOfType(IllegalStateException.class)
//                .isThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()));
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");


    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_ConfigurationOk_And_noProjects_throwsIllegalArgumentException() {
        //given (miejsce w, którym przygotowujemy dane)
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(null, mockRepository, null, mockConfig);


        // when (gdy wołamy testowaną metodę)
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
        //then (czy metoda wyskutkowała to co założyliśmy)
//        assertThatExceptionOfType(IllegalStateException.class)
//                .isThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()));


        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");


    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_noProjects_throwsIllegalArgumentException() {
        //given (miejsce w, którym przygotowujemy dane)
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockGroupRepository, mockRepository, null, mockConfig);


        // when (gdy wołamy testowaną metodę)
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
        //then (czy metoda wyskutkowała to co założyliśmy)
//        assertThatExceptionOfType(IllegalStateException.class)
//                .isThrownBy(() -> toTest.createGroup(0, LocalDateTime.now()));


        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");


    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createAndSavesGroup() {
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        var project =  projectWith("bar", Set.of(-1,-2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //and
        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var serviceWithInMemRepo = dummyGroupService(inMemoryGroupRepo);
        var countBeforeCall = inMemoryGroupRepo.count();
        //and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //system under test
        var toTest = new ProjectService(inMemoryGroupRepo, mockRepository, serviceWithInMemRepo, mockConfig);

        //when
        GroupReadModel result = toTest.createGroup(1, today);

        //then
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall).isEqualTo(inMemoryGroupRepo.count());
    }

    private static TaskGroupService dummyGroupService(InMemoryGroupRepository inMemoryGroupRepo) {
        return new TaskGroupService(inMemoryGroupRepo, null);
    }

    private Project projectWith (String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());
        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    }


    private static class InMemoryGroupRepository implements TaskGroupRepository {

            private int index = 0;
            private Map<Integer, TaskGroup> map = new HashMap<>();

            public int count() {
                return map.values().size();
            }

            @Override
            public List<TaskGroup> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                if(entity.getId() == 0){
                    try {
                        var field = TaskGroup.class.getDeclaredField("id");
                        field.setAccessible(true);
                        field.set(entity, ++index);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    map.put(entity.getId(), entity);
                }
                return entity;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return map.values().stream()
                        .filter(group -> !group.isDone())
                        .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
            }
        };

    }


