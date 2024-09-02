package com.task_organizer.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Task title can not be empty !")
    private String title;

    private String description;

    @NotBlank
    private Date dueDate;

    @NotBlank(message = "Task must be assigned before creating.")
    @Email
    private String assignee;

    private List<String> attachments;


}
