package com.example.api.validation.Users;

import com.example.api.models.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StoreUserDTO {
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

    public User toUser() {
        User user = new User();

        user.setUsername(username);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setEmail(email);

        return user;
    }
}
