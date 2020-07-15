package pl.Pakinio.KursUdemy.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.Pakinio.KursUdemy.model.TaskGroup;
import pl.Pakinio.KursUdemy.model.TaskGroupRepository;
import pl.Pakinio.KursUdemy.model.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw when undone tasks")
    void toggleGroup_throwsIllegalStateException(){
        //given
        TaskRepository mockTaskRepository=taskRepositoryReturning(true);

        TaskGroupService toTest=new TaskGroupService(null,mockTaskRepository);

        Throwable exception = catchThrowable(() -> toTest.toggleGroup(1));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");
    }

    @Test
    @DisplayName("should throw when no group")
    void toggleGroup_wrongId_throwsIllegalArgumentException(){
        TaskRepository mockTaskRepository=taskRepositoryReturning(false);

        TaskGroupRepository mockRepository=mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskGroupService toTest=new TaskGroupService(mockRepository,mockTaskRepository);

        Throwable exception = catchThrowable(() -> toTest.toggleGroup(1));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should ttoggle group")
    void toggleGroup_worksAsExpected(){
        TaskRepository mockTaskRepository=taskRepositoryReturning(false);

        TaskGroup group=new TaskGroup();
        boolean beforeToggle = group.isDone();

        TaskGroupRepository mockRepository=mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(group));

        TaskGroupService toTest=new TaskGroupService(mockRepository,mockTaskRepository);

        toTest.toggleGroup(0);

        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }


    private TaskRepository taskRepositoryReturning(final boolean result){
        TaskRepository mockTaskRepository=mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}