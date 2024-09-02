package com.task_organizer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


import com.task_organizer.exception.UserAlreadyExistsException;
import com.task_organizer.model.User;
import com.task_organizer.payload.request.RegisterUserRequest;
import com.task_organizer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterNewUser_Success() throws UserAlreadyExistsException {
        RegisterUserRequest request = getRequest();
        User user = getUser();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("testPW");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerNewUser(request);

        assertEquals("ankit@test.com", registeredUser.getEmail());
        assertEquals("Super User", registeredUser.getName());
        assertEquals("testPW", registeredUser.getPassword());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterNewUser_UserAlreadyExists() {
        RegisterUserRequest request = getRequest();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerNewUser(request));
    }

    User getUser(){
        return new User("ankit@test.com"
                , "Super User"
                , "testPW");
    }

    RegisterUserRequest getRequest(){
        return new RegisterUserRequest("ankit@test.com"
                , "Super User"
                , "testPW");
    }
}