package com.task_organizer.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    @Email
    private String email;

    @Size(min = 6, max = 10, message = "Password must be between 6 and 10 characters")
    private String password;
}
