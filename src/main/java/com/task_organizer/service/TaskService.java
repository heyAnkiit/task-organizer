package com.task_organizer.service;

import com.task_organizer.annotations.Log;
import com.task_organizer.configuration.AuditConfiguration;
import com.task_organizer.exception.NotEnoughPermissionException;
import com.task_organizer.exception.TaskNotFoundException;
import com.task_organizer.model.Task;
import com.task_organizer.model.TaskComments;
import com.task_organizer.payload.request.AddCommentRequest;
import com.task_organizer.payload.request.CreateTaskRequest;
import com.task_organizer.payload.request.TransitionTaskStatusRequest;
import com.task_organizer.payload.request.UpdateTaskRequest;
import com.task_organizer.repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuditConfiguration auditConfiguration;

    @Log
    public Task createTask(CreateTaskRequest request) {
        Task task = modelMapper.map(request, Task.class);
        return taskRepository.save(task);
    }

    @Log
    public Task getTaskById(Long taskId) throws NotEnoughPermissionException, TaskNotFoundException {
        Task task = getTask(taskId);
        String loggedInUser = auditConfiguration.getLoggedInUser();

        if (!task.getAssignee().equalsIgnoreCase(loggedInUser) &&
                !task.getCreatedBy().equalsIgnoreCase(loggedInUser)) {
            throw new NotEnoughPermissionException("You do not have enough permissions to view this task.");
        }
        return task;
    }

    @Log
    public List<Task> getTasks() {
        String loggedInUser = auditConfiguration.getLoggedInUser();
        return taskRepository.findTasksForLoggedInUser(loggedInUser);
    }

    public void deleteTaskByIdAndLoggedInUser(Long taskId) {
        String loggedInUser = auditConfiguration.getLoggedInUser();
        taskRepository.deleteTaskById(taskId ,loggedInUser);
    }

    @Log
    public void deleteTaskById(Long taskId) throws TaskNotFoundException, NotEnoughPermissionException {
        Task task = getTask(taskId);
        String loggedInUser = auditConfiguration.getLoggedInUser();
        if (!task.getCreatedBy().equalsIgnoreCase(loggedInUser)) {
            throw new NotEnoughPermissionException("You do not have enough permissions to delete this task.");
        }
        taskRepository.deleteById(taskId);
    }

    @Log
    public Task updateTask(Long taskId, UpdateTaskRequest request)
            throws TaskNotFoundException {

        Task task = getTask(taskId);
        modelMapper.map(request , task);

        if (request.getAttachments() != null) {
            task.setAttachments(new ArrayList<>(request.getAttachments()));
        }
        return taskRepository.save(task);
    }

    @Log
    public void transitionStatus(Long taskId, TransitionTaskStatusRequest request)
            throws TaskNotFoundException {
        Task task = getTask(taskId);
        task.setStatus(request.getStatus());
        taskRepository.save(task);
    }

    private Task getTask(Long taskId)
            throws TaskNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with id:  " + taskId + " is not found !"));
    }

    @Log
    public Task addComment(Long taskId, AddCommentRequest request)
            throws TaskNotFoundException {
        Task task = getTask(taskId);
        String loggedInUser = auditConfiguration.getLoggedInUser();
        List<TaskComments> comments = task.getComments();

        if (comments == null || comments.isEmpty()) {
            List<TaskComments> list = new ArrayList<>();
            list.add(new TaskComments(request.getComment(), loggedInUser));
            task.setComments(list);
        } else {
            task.getComments().add(new TaskComments(request.getComment(), loggedInUser));
        }
        return taskRepository.save(task);
    }
}
