package pl.Pakinio.KursUdemy.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.Pakinio.KursUdemy.model.Project;
import pl.Pakinio.KursUdemy.model.ProjectRepository;
import pl.Pakinio.KursUdemy.model.TaskGroup;
import pl.Pakinio.KursUdemy.model.TaskGroupRepository;

import java.util.List;

@Repository
public interface SqlProjectRepository extends ProjectRepository,JpaRepository<Project,Integer> {

    @Override
    @Query("SELECT distinct p FROM Project p join fetch p.steps")
    List<Project> findAll();

}
