package pl.Pakinio.KursUdemy.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.Pakinio.KursUdemy.TaskConfigurationProperties;
import pl.Pakinio.KursUdemy.model.*;
import pl.Pakinio.KursUdemy.model.projection.GroupReadModel;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_And_openGroupsExists_throwsIllegalStateException() {
        TaskGroupRepository mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);

        //assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500));

        TaskConfigurationProperties.Template mockTemplete = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplete.isAllowMultipleTasks()).thenReturn(false);


        TaskConfigurationProperties mocConfig = mock(TaskConfigurationProperties.class);
        when(mocConfig.getTemplate()).thenReturn(mockTemplete);

        ProjectService toTest = new ProjectService(null, mockGroupRepository, mocConfig,null);

        /*assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(()->toTest.createGroup(LocalDateTime.now(), 0));*/

        /*assertThatIllegalStateException()
                .isThrownBy(()->toTest.createGroup(LocalDateTime.now(), 0));*/

        Throwable exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");

    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {
        ProjectRepository mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mocConfig = configurationReturning(true);

        ProjectService toTest = new ProjectService(mockRepository, null, mocConfig,null);


        Throwable exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configurd to allow just 1 group and no groups and projects for a given id")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupsExists_noProjects_throwsIllegalArgumentException() {
        ProjectRepository mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);

        TaskConfigurationProperties mocConfig = configurationReturning(true);

        ProjectService toTest = new ProjectService(mockRepository, mockGroupRepository, mocConfig,null);


        Throwable exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");

    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup() {
        LocalDateTime today= LocalDate.now().atStartOfDay();

        Set<Integer>set=new HashSet<>();
        set.add(-1);
        set.add(-2);

        Project project = projectWith("bar", set);

        ProjectRepository mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));

        InMemoryGroupRepository inMemoryGroupRepo=inMemoryGroupRepository();
        TaskGroupService serviceWithInMemory=dummyGroupService(inMemoryGroupRepo);

        int countBeforeCall=inMemoryGroupRepo.count();

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        ProjectService toTest=new ProjectService(mockRepository,inMemoryGroupRepo,mockConfig,serviceWithInMemory);

        GroupReadModel result=toTest.createGroup(today,1);

        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task->task.getDescription().equals("foo"));
        assertThat(countBeforeCall+1).isEqualTo(inMemoryGroupRepo.count());
    }

    private TaskGroupService dummyGroupService(final InMemoryGroupRepository inMemoryGroupRepository){
        return new TaskGroupService(inMemoryGroupRepository,null);
    }

    private Project projectWith(String description,Set<Integer> daysToDeadline){

        Set<ProjectStep>steps=daysToDeadline.stream()
                .map(days->{
                    ProjectStep step=mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("foo");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                }).collect(Collectors.toSet());


        Project result=mock(Project.class);
        when(result.getDescription()).thenReturn(description);
        when(result.getSteps()).thenReturn(steps);

        return result;
    }

    private TaskGroupRepository groupRepositoryReturning(final boolean result) {
        TaskGroupRepository mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(final boolean result) {
        TaskConfigurationProperties.Template mockTemplete = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplete.isAllowMultipleTasks()).thenReturn(result);

        TaskConfigurationProperties mocConfig = mock(TaskConfigurationProperties.class);
        when(mocConfig.getTemplate()).thenReturn(mockTemplete);
        return mocConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository() {

        };
    }
    private static class InMemoryGroupRepository implements TaskGroupRepository{
        private int index=0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count(){
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
            if (entity.getId() == 0) {
                try {
                    Field field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity,++index);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            map.put(entity.getId(),entity);

            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group->!group.isDone())
                    .allMatch(group->group.getProject() != null && group.getProject().getId()==projectId);
        }

        @Override
        public boolean existsByDescription(String description) {
            return map.values().stream()
                    .allMatch(group->group.getDescription().equals(description));
        }
    }
}