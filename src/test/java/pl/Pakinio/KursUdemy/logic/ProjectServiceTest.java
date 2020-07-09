package pl.Pakinio.KursUdemy.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.Pakinio.KursUdemy.TaskConfigurationProperties;
import pl.Pakinio.KursUdemy.model.TaskGroupRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_Anf_openGroupsExists_throwsIllegalStateException() {
        TaskGroupRepository mockGroupRepository=mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);

        //assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500));

        TaskConfigurationProperties.Template mockTemplete=mock(TaskConfigurationProperties.Template.class);
        when(mockTemplete.isAllowMultipleTasks()).thenReturn(false);


        TaskConfigurationProperties mocConfig=mock(TaskConfigurationProperties.class);
        when(mocConfig.getTemplate()).thenReturn(mockTemplete);

        ProjectService toTest=new ProjectService(null,mockGroupRepository,mocConfig);

        toTest.createGroup(LocalDateTime.now(),0);
    }
}