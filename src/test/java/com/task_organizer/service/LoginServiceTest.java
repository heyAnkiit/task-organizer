package com.task_organizer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.task_organizer.exception.InvalidCredentialsException;
import com.task_organizer.model.User;
import com.task_organizer.payload.request.LoginRequest;
import com.task_organizer.utility.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private LoginService loginService;

    @Mock
    private Authentication authentication;

    @Test
    void testAuthenticateUser_Success() throws InvalidCredentialsException {
        LoginRequest loginRequest = getLoginRequest();
        User user = getUser();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(user);

        when(jwtUtils.generateToken(user.getEmail())).thenReturn("t0|<3n");

        String token = loginService.authenticateUser(loginRequest);

        assertEquals("t0|<3n", token);
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        LoginRequest loginRequest = getLoginRequest();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(InvalidCredentialsException.class, () -> loginService.authenticateUser(loginRequest));
    }

    LoginRequest getLoginRequest(){
        return new LoginRequest("ankit@test.com"
                , "testPW");
    }

    User getUser(){
        return new User("ankit@test.com"
                , "Super User"
                , "testPW");
    }
}