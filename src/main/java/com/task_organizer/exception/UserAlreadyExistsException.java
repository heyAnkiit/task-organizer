package com.task_organizer.exception;

public class UserAlreadyExistsException extends Exception{

        public UserAlreadyExistsException(String message){super(message);}

        public UserAlreadyExistsException(String message , Throwable cause){super(message , cause);}
}
