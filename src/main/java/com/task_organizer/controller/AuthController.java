package com.task_organizer.controller;


import com.task_organizer.exception.InvalidCredentialsException;
import com.task_organizer.exception.UserAlreadyExistsException;
import com.task_organizer.model.User;
import com.task_organizer.payload.request.LoginRequest;
import com.task_organizer.payload.request.RegisterUserRequest;
import com.task_organizer.service.LoginService;
import com.task_organizer.service.UserService;
import com.task_organizer.utility.ApiResponse;
import com.task_organizer.utility.ApplicationConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/play")
    public ResponseEntity<String> playGround() {
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerNewUser(@Valid @RequestBody RegisterUserRequest request) {
        try {
            User user = userService.registerNewUser(request);
            return new ResponseEntity<>
                    (new ApiResponse(201,  ApplicationConstants.SUCCESS, user)
                            , HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>
                    (new ApiResponse(409,  ApplicationConstants.FAILED, "User with email : " + request.getEmail() +
                            " is already registered make sure you are entering correct details.")
                            , HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            String generatedToken = loginService.authenticateUser(request);
            return new ResponseEntity<>
                    (new ApiResponse(200,  ApplicationConstants.SUCCESS, generatedToken)
                            , HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>
                    (new ApiResponse(401,  ApplicationConstants.FAILED, "Invalid Credentials! Make sure that you are entering the correct credentials")
                            , HttpStatus.UNAUTHORIZED);
        }
    }


}
