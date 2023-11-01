package com.example.api.services.User;

import java.util.List;

import com.example.api.models.Conversation;
import com.example.api.models.User;
import com.example.api.validation.Auth.UserLoginDTO;
import com.example.api.validation.Users.UserDto;

public interface IUserService {
    List<UserDto> findAllUsers();
    List<Conversation> findAllConversationsFromUser(Integer userId);
    User login(UserLoginDTO userLoginDTO);
    void saveUser(UserDto userDto);
}
