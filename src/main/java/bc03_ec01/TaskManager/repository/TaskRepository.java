package bc03_ec01.TaskManager.repository;

import bc03_ec01.TaskManager.model.Task;
import bc03_ec01.TaskManager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  Page<Task> findByUser(User user, Pageable pageable);
}
