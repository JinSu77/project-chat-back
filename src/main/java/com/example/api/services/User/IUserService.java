package com.example.api.services.User;

import java.util.List;

import com.example.api.dtos.Auth.UserLoginDTO;
import com.example.api.dtos.Users.UserDto;
import com.example.api.models.Conversation;
import com.example.api.models.User;

public interface IUserService {
    List<UserDto> findAllUsers();
    List<User> findAllById(List<Integer> userIds);
    List<Conversation> findAllConversationsFromUser(Integer userId);
    User login(UserLoginDTO userLoginDTO);
    void save(UserDto userDto);
    List<User> getRandomContactListFor(User user);
    void deleteUserContact(Integer userId, Integer contactId);
}
