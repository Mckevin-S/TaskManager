package bc03_ec01.TaskManager.service;

import bc03_ec01.TaskManager.model.User;
import bc03_ec01.TaskManager.payload.request.TaskRequest;
import bc03_ec01.TaskManager.payload.response.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
  Page<TaskResponse> getAllTasks(User user, Pageable pageable);

  TaskResponse getTaskById(Long id, User user);

  TaskResponse createTask(TaskRequest taskRequest, User user);

  TaskResponse updateTask(Long id, TaskRequest taskRequest, User user);

  void deleteTask(Long id, User user);
}
