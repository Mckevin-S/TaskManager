package bc03_ec01.TaskManager.service;

import bc03_ec01.TaskManager.model.Task;
import bc03_ec01.TaskManager.model.User;
import bc03_ec01.TaskManager.payload.request.TaskRequest;
import bc03_ec01.TaskManager.payload.response.TaskResponse;
import bc03_ec01.TaskManager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Page<TaskResponse> getAllTasks(User user, Pageable pageable) {
        return taskRepository.findByUser(user, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public TaskResponse getTaskById(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to access this task");
        }

        return mapToResponse(task);
    }

    @Override
    public TaskResponse createTask(TaskRequest taskRequest, User user) {
        Task task = new Task(
                taskRequest.getTitle(),
                taskRequest.getDescription(),
                taskRequest.getStatus(),
                user
        );

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest taskRequest, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to update this task");
        }

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    @Override
    public void deleteTask(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this task");
        }

        taskRepository.delete(task);
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        );
    }
}
