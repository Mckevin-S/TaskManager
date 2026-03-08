package bc03_ec01.TaskManager.controller;

import bc03_ec01.TaskManager.model.User;
import bc03_ec01.TaskManager.payload.request.TaskRequest;
import bc03_ec01.TaskManager.payload.response.MessageResponse;
import bc03_ec01.TaskManager.payload.response.TaskResponse;
import bc03_ec01.TaskManager.repository.UserRepository;
import bc03_ec01.TaskManager.security.services.UserDetailsImpl;
import bc03_ec01.TaskManager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Endpoints for managing user tasks")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get all tasks for the authenticated user")
    public ResponseEntity<Page<TaskResponse>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User user = getCurrentUser();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getAllTasks(user, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific task by ID")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        User user = getCurrentUser();
        return ResponseEntity.ok(taskService.getTaskById(id, user));
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        User user = getCurrentUser();
        return ResponseEntity.ok(taskService.createTask(taskRequest, user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest) {
        User user = getCurrentUser();
        return ResponseEntity.ok(taskService.updateTask(id, taskRequest, user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        User user = getCurrentUser();
        taskService.deleteTask(id, user);
        return ResponseEntity.ok(new MessageResponse("Task deleted successfully!"));
    }

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
