package com.task_organizer.payload.request;

import com.task_organizer.enums.TaskStatus;
import lombok.Data;

@Data
public class TransitionTaskStatusRequest {
    private TaskStatus status;
}
