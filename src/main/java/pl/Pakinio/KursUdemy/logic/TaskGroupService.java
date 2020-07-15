package pl.Pakinio.KursUdemy.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.Pakinio.KursUdemy.TaskConfigurationProperties;
import pl.Pakinio.KursUdemy.model.*;
import pl.Pakinio.KursUdemy.model.projection.GroupReadModel;
import pl.Pakinio.KursUdemy.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

//@Service
public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    TaskGroupService(final TaskGroupRepository repository,final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository=taskRepository;
    }

    public GroupReadModel createGroup(final GroupWriteModel source) {
        return createGroup(source,null);
    }

    GroupReadModel  createGroup(GroupWriteModel source, Project project) {
         TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }
    public void toggleGroup(int groupId){
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks. Done all the tasks first");
        }
        TaskGroup result=repository.findById(groupId)
                .orElseThrow(()->new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }


}
