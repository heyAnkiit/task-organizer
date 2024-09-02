package com.task_organizer.enums;

public enum TaskStatus {
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED");

    private String value;

    private TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
