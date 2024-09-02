package com.task_organizer.exception;

public class InvalidCredentialsException extends Exception{

    public InvalidCredentialsException(String message){super(message);}

    public InvalidCredentialsException(String message , Throwable cause){super(message , cause);}
}
