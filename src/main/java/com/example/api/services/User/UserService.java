package com.example.api.services.User;

import java.util.List;

import com.example.api.models.User;
import com.example.api.validation.Auth.UserLoginDTO;
import com.example.api.validation.Users.UserDto;

public interface UserService {
    void saveUser(UserDto userDto);

    List<UserDto> findAllUsers();

    User login(UserLoginDTO userLoginDTO);
}
