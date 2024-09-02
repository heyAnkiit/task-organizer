package com.task_organizer.exception;

public class TaskNotFoundException  extends Exception{

    public TaskNotFoundException(String message){super(message);}

    public TaskNotFoundException(String message , Throwable cause){super(message , cause);}
}
