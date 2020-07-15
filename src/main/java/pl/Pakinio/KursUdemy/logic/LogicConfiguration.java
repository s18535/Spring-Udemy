package pl.Pakinio.KursUdemy.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.Pakinio.KursUdemy.TaskConfigurationProperties;
import pl.Pakinio.KursUdemy.model.ProjectRepository;
import pl.Pakinio.KursUdemy.model.TaskGroupRepository;
import pl.Pakinio.KursUdemy.model.TaskRepository;

@Configuration
public class LogicConfiguration {

    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfigurationProperties config,
            final TaskGroupService taskGroupService
            ){
        return new ProjectService(repository,taskGroupRepository,config,taskGroupService);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository taskGroupRepository,
            final TaskRepository taskRepository
            ){
        return new TaskGroupService(taskGroupRepository,taskRepository);
    }
}
