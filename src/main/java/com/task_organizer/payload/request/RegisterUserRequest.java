package com.task_organizer.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class RegisterUserRequest {
    @Email
    private String email;

    private String name;

    @Size(min = 6, max = 10, message = "Password must be between 6 and 10 characters")
    private String password;
}
