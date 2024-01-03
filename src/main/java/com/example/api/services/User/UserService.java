package com.example.api.services.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.dtos.Auth.UserLoginDTO;
import com.example.api.dtos.Users.UserDto;
import com.example.api.models.Conversation;
import com.example.api.models.Role;
import com.example.api.models.User;
import com.example.api.repositories.RoleRepository;
import com.example.api.repositories.UserRepository;
import com.example.api.services.Conversation.ConversationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConversationService conversationService;

    public UserService(
        UserRepository userRepository, 
        RoleRepository roleRepository, 
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByUsername(userLoginDTO.getUsername());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Username %s not found", userLoginDTO.getUsername()));
        }
        
        if (! passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid password");
        }

        return user;
    }

    @Override
    public void save(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Username %s already exist", userDto.getUsername()));
        }

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, String.format("Email %s already exist", userDto.getEmail()));
        }

        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("USER");

        if(role == null){
            role = checkRoleExist();
        }

        user.setRoles(Arrays.asList(role));

        userRepository.save(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllById(List<Integer> userIds) {
        Iterable <User> users = userRepository.findAllById(userIds);

        return (List<User>) users;
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();

        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());

        return userDto;
    }
    
    private Role checkRoleExist(){
        Role role = new Role();

        role.setName("USER");

        return roleRepository.save(role);
    }

    @Override
    public List<Conversation> findAllConversationsFromUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return user.get().getConversations();
    }

    public List<User> addContactList(Integer userId, Integer contactId) {
            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<User> optionalContact = userRepository.findById(contactId);

            if (optionalUser.isEmpty() || optionalContact.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or contact not found");
            }

            User user = optionalUser.get();
            User contact = optionalContact.get();

            if (user.getContacts().contains(contact) || contact.getContacts().contains(user)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Contact already exist");
            }

            user.getContacts().add(contact);
            contact.getContacts().add(user);

            userRepository.save(user);
            userRepository.save(contact);

            conversationService.createConversationBetweenUserAndContact(user, contact);

            return user.getContacts();
    }

    public List<User> getContactList(Integer userId) {
        
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            User user = optionalUser.get();

            return user.getContacts();
    }

    public List<User> getRandomContactListFor(User user) {
        List<User> randomContacts = new ArrayList<User>();


        List<String> contactIds = user.getContacts().stream().map(contact -> contact.getId().toString()).collect(Collectors.toList());

        String query;

        if (contactIds.size() != 0) {
            query = "SELECT * FROM users WHERE username != '" + user.getUsername() 
                + "' AND id NOT IN (" + String.join(",", contactIds) + ")"
                + " ORDER BY RAND() LIMIT 5";

        } else {
            query = "SELECT * FROM users WHERE username != '" + user.getUsername() 
                + "' ORDER BY RAND() LIMIT 5";
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        for (Map<String, Object> row : rows) {
            User contact = userRepository.findById((Integer) row.get("id")).get();

            randomContacts.add(contact);
        }

        return randomContacts;
    }
}
