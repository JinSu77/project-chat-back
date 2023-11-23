package com.example.api.services.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Contact;
import com.example.api.models.Conversation;
import com.example.api.models.Role;
import com.example.api.models.User;
import com.example.api.repositories.ContactRepository;
import com.example.api.repositories.RoleRepository;
import com.example.api.repositories.UserRepository;
import com.example.api.validation.Auth.UserLoginDTO;
import com.example.api.validation.Users.UserDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ContactRepository contactRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ContactRepository contactRepository) {
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

        Contact contact = new Contact();

        contact.setUsername(user.getUsername());
        contact.setFirstName(user.getFirstName());
        contact.setLastName(user.getLastName());
        contact.setEmail(user.getEmail());
        contact.setUserId(user.getId());
        
        user.setContacts(contact);

        contactRepository.save(contact);
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

    public ResponseEntity<Object> addContactList(Integer userId, Integer contactId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "User not found");
            }

            User user = optionalUser.get();

            Optional<Contact> optionalContact = contactRepository.findById(contactId);

            if (optionalContact.isEmpty()) {
                return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "Contact not found");
            }        

            Contact contact = optionalContact.get();

            user.setContacts(contact);

            userRepository.save(user);

            return ResponseHandler.generateResponse(HttpStatus.OK, "contact", contact);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
