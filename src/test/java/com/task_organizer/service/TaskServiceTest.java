package com.task_organizer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task_organizer.configuration.AuditConfiguration;
import com.task_organizer.enums.TaskStatus;
import com.task_organizer.exception.NotEnoughPermissionException;
import com.task_organizer.exception.TaskNotFoundException;
import com.task_organizer.model.Task;
import com.task_organizer.payload.request.AddCommentRequest;
import com.task_organizer.payload.request.CreateTaskRequest;
import com.task_organizer.payload.request.TransitionTaskStatusRequest;
import com.task_organizer.payload.request.UpdateTaskRequest;
import com.task_organizer.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private AuditConfiguration auditConfiguration;

    @Test
    void testCreateTask_Success() {
        CreateTaskRequest createTaskRequest = getCreateTaskRequest();
        Task task = getTask();

        when(modelMapper.map(createTaskRequest, Task.class)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(createTaskRequest);

        assertEquals(task.getId(), createdTask.getId());
        assertEquals(task.getTitle(), createdTask.getTitle());
        assertEquals(task.getDescription(), createdTask.getDescription());

        verify(modelMapper).map(createTaskRequest, Task.class);
        verify(taskRepository).save(task);
    }


    @Test
    void testGetTaskById_Success() throws NotEnoughPermissionException, TaskNotFoundException {
        Long taskId = 1L;
        Task task = getTask();
        String loggedInUser = "johnDoe";

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(auditConfiguration.getLoggedInUser()).thenReturn(loggedInUser);

        Task foundTask = taskService.getTaskById(taskId);

        assertEquals(task.getId(), foundTask.getId());
        assertEquals(task.getAssignee(), foundTask.getAssignee());
        assertEquals(task.getCreatedBy(), foundTask.getCreatedBy());

        verify(taskRepository).findById(taskId);
        verify(auditConfiguration).getLoggedInUser();
    }

    @Test
    void testGetTaskById_NotEnoughPermission() {
        Long taskId = 1L;
        Task task = getTask();
        String loggedInUser = "doeJohn";

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(auditConfiguration.getLoggedInUser()).thenReturn(loggedInUser);

        assertThrows(NotEnoughPermissionException.class, () -> taskService.getTaskById(taskId));
    }

    @Test
    void testGetTaskById_TaskNotFound() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));

        verify(taskRepository).findById(taskId);
    }

    @Test
    void testGetTasks_Success() {
        String loggedInUser = "johnDoe";
        Task task1 = getTask();
        task1.setTitle("Task 1");
        Task task2 = getTask();
        task2.setTitle("Task 2");

        List<Task> taskList = Arrays.asList(task1, task2);

        when(auditConfiguration.getLoggedInUser()).thenReturn(loggedInUser);
        when(taskRepository.findTasksForLoggedInUser(loggedInUser)).thenReturn(taskList);

        List<Task> tasks = taskService.getTasks();

        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());

        verify(auditConfiguration).getLoggedInUser();
        verify(taskRepository).findTasksForLoggedInUser(loggedInUser);
    }

    @Test
    void testGetTasks_EmptyList() {
        String loggedInUser = "johnDoe";

        when(auditConfiguration.getLoggedInUser()).thenReturn(loggedInUser);
        when(taskRepository.findTasksForLoggedInUser(loggedInUser)).thenReturn(List.of());

        List<Task> tasks = taskService.getTasks();

        assertTrue(tasks.isEmpty());

        verify(auditConfiguration).getLoggedInUser();
        verify(taskRepository).findTasksForLoggedInUser(loggedInUser);
    }

    @Test
    void testDeleteTaskById() {
        Long taskId = 1L;
        taskService.deleteTaskById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void testUpdateTask_Success() throws TaskNotFoundException {
        Task updatedTask = getTask();
        updatedTask.setTitle("Updated Task");

        UpdateTaskRequest updateTaskRequest = getUpdateTaskRequest();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(updatedTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        updatedTask = taskService.updateTask(1L, updateTaskRequest);

        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals(2, updatedTask.getAttachments().size());
        assertTrue(updatedTask.getAttachments().contains("attachment1"));
        assertTrue(updatedTask.getAttachments().contains("attachment2"));

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(updatedTask);
    }


    @Test
    void testTransitionStatus_Success() throws TaskNotFoundException {
        Task task = getTask();
        TransitionTaskStatusRequest request = getTransitionTaskStatusRequest();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.transitionStatus(1L, request);

        assertEquals(TaskStatus.COMPLETED, task.getStatus());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
    }


    @Test
    void testAddComment() throws TaskNotFoundException {
        Task task = getTask();
        AddCommentRequest request = getAddCommentRequest();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(auditConfiguration.getLoggedInUser()).thenReturn("testUser");

        Task updatedTask = taskService.addComment(1L, request);

        System.out.println(updatedTask);
        assertNotNull(updatedTask.getComments());
        assertEquals(1, updatedTask.getComments().size());
        assertEquals("New comment", updatedTask.getComments().get(0).getComment());
        assertEquals("testUser", updatedTask.getComments().get(0).getAddedBy());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
    }

    AddCommentRequest getAddCommentRequest() {
        AddCommentRequest request = new AddCommentRequest();
        request.setComment("New comment");
        return request;
    }


    TransitionTaskStatusRequest getTransitionTaskStatusRequest(){
        TransitionTaskStatusRequest request = new TransitionTaskStatusRequest();
        request.setStatus(TaskStatus.COMPLETED);
        return request;
    }

    CreateTaskRequest getCreateTaskRequest(){
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setTitle("New Task");
        createTaskRequest.setDescription("Task Description");
        return createTaskRequest;
    }

    UpdateTaskRequest getUpdateTaskRequest(){
        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setTitle("Updated Task");
        updateRequest.setAttachments(List.of("attachment1", "attachment2"));
        return updateRequest;
    }

    Task getTask(){
        Task task = new Task();
        task.setId(1L);
        task.setTitle("New Task");
        task.setDescription("Task Description");
        task.setAssignee("johnDoe");
        task.setCreatedBy("johnDoe");
        return task;
    }
}
