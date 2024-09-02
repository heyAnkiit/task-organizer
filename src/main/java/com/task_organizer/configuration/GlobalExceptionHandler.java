package com.task_organizer.configuration;

import com.task_organizer.utility.ApiResponse;
import com.task_organizer.utility.ApplicationConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse> handleSignatureException(SignatureException ex) {
        return new ResponseEntity<>(new ApiResponse(401, ApplicationConstants.FAILED,  ex.getMessage())
                , HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse(500, ApplicationConstants.FAILED,  ex.getMessage())
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
