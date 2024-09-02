package com.task_organizer.controller;

import com.task_organizer.exception.NotEnoughPermissionException;
import com.task_organizer.exception.TaskNotFoundException;
import com.task_organizer.model.Task;
import com.task_organizer.payload.request.AddCommentRequest;
import com.task_organizer.payload.request.CreateTaskRequest;
import com.task_organizer.payload.request.UpdateTaskRequest;
import com.task_organizer.payload.request.TransitionTaskStatusRequest;
import com.task_organizer.service.TaskService;
import com.task_organizer.utility.ApiResponse;
import com.task_organizer.utility.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse> createTask(@RequestBody CreateTaskRequest request){
        Task task = taskService.createTask(request);
        return new ResponseEntity<>(new ApiResponse(201,  ApplicationConstants.SUCCESS, task), HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse> getTaskById(@PathVariable Long taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            return new ResponseEntity<>(new ApiResponse(200,  ApplicationConstants.SUCCESS, task), HttpStatus.OK);
        } catch (NotEnoughPermissionException e) {
            return new ResponseEntity<>(new ApiResponse(403,  ApplicationConstants.FAILED, e.getMessage()), HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(404,  ApplicationConstants.FAILED, "Task not found"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getTasks(){
        List<Task> tasks = taskService.getTasks();
        return new ResponseEntity<>(new ApiResponse(200,  ApplicationConstants.SUCCESS , tasks) ,HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse> deleteTaskById(@PathVariable Long taskId) {
        try {
            taskService.deleteTaskById(taskId);
            return new ResponseEntity<>(new ApiResponse(200, ApplicationConstants.SUCCESS, ""), HttpStatus.OK);
        } catch (NotEnoughPermissionException e) {
            return new ResponseEntity<>(new ApiResponse(403, ApplicationConstants.FAILED, e.getMessage()), HttpStatus.FORBIDDEN);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(404, ApplicationConstants.FAILED, "Task not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse> updateTask(@PathVariable Long taskId , @RequestBody UpdateTaskRequest request)
            throws TaskNotFoundException {
        Task task = taskService.updateTask(taskId, request);
        return new ResponseEntity<>(new ApiResponse(200 ,  ApplicationConstants.SUCCESS ,task),HttpStatus.OK);
    }

    @PutMapping("/{taskId}/transition")
    public ResponseEntity<ApiResponse> transitionStatus(@PathVariable Long taskId , @RequestBody TransitionTaskStatusRequest request)
            throws TaskNotFoundException {
        taskService.transitionStatus(taskId , request);
        return new ResponseEntity<>(new ApiResponse(200 ,  ApplicationConstants.SUCCESS ,"Task transitioned successfully to : " + request.getStatus())
                ,HttpStatus.OK);
    }

    @PutMapping("/{taskId}/comment")
    public ResponseEntity<ApiResponse> addComment(@PathVariable Long taskId , @RequestBody AddCommentRequest request) throws TaskNotFoundException {
        Task task = taskService.addComment(taskId, request);
        return new ResponseEntity<>(new ApiResponse(200 ,  ApplicationConstants.SUCCESS ,task)
                ,HttpStatus.OK);
    }

}
