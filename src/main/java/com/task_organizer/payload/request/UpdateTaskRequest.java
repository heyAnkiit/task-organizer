package com.task_organizer.payload.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private Date dueDate;
    private String assignee;
    private List<String> attachments;
}
