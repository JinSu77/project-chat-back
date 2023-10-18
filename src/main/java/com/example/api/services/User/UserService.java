package com.example.api.services.User;

import java.util.List;

import com.example.api.models.User;
import com.example.api.validation.Users.UserDto;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByUsername(String username);

    List<UserDto> findAllUsers();
}
