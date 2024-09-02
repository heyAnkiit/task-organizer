package com.task_organizer.exception;

public class NotEnoughPermissionException extends Exception{
    public NotEnoughPermissionException(String message){super(message);}

    public NotEnoughPermissionException(String message , Throwable cause){super(message , cause);}
}
