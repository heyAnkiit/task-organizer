package com.task_organizer.service;

import com.task_organizer.annotations.Log;
import com.task_organizer.exception.UserAlreadyExistsException;
import com.task_organizer.model.User;
import com.task_organizer.payload.request.RegisterUserRequest;
import com.task_organizer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Log
    public User registerNewUser(RegisterUserRequest request) throws UserAlreadyExistsException {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new UserAlreadyExistsException("User with email: " + request.getEmail() + " is already registered.");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), request.getName(), encodedPassword);
        return userRepository.save(user);
    }
}
