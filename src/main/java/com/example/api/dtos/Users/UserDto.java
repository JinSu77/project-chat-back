package com.example.api.dtos.Users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotBlank(message = "Last name is required")
    @Size(min = 4, max = 20, message = "Last name must be between 4 and 20 characters")
    private String lastName;

    @NotBlank(message = "First name is required")
    @Size(min = 4, max = 20, message = "First name must be between 4 and 20 characters")
    private String firstName;

    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Size(min = 4, max = 40, message = "Email must be between 4 and 40 characters")
    private String email;
}
