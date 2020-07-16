package pl.Pakinio.KursUdemy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import pl.Pakinio.KursUdemy.model.Task;
import pl.Pakinio.KursUdemy.model.TaskGroup;
import pl.Pakinio.KursUdemy.model.TaskGroupRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger= LoggerFactory.getLogger(Warmup.class);

    private final TaskGroupRepository groupRepository;

    public Warmup(TaskGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed");
        final String description="ApplicationContextEvent";
        if (!groupRepository.existsByDescription(description)){
            logger.info("No required group found! Adding it!");
            TaskGroup group=new TaskGroup();
            group.setDescription(description);
            Set<Task> set=new HashSet<>();
            set.add(new Task("ContextClosedEvent",null,group));
            set.add(new Task("ContextRefreshedEvent",null,group));
            set.add(new Task("ContextStoppedEvent",null,group));
            set.add(new Task("ContextStartedEvent",null,group));

            group.setTasks(set);
            groupRepository.save(group);
        }
    }
}
