package pl.Pakinio.KursUdemy.reports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersistedTaskEventRepository extends JpaRepository<PersistedTaskEvent,Integer> {
    List<PersistedTaskEvent> findByTaskId(int taskId);
}
