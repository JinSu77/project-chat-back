package com.example.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.exceptions.HubNotFoundException;
import com.example.api.exceptions.PublishRejectedException;
import com.example.api.exceptions.UnauthorizedPublisherException;
import com.example.api.handlers.ResponseHandler;
import com.example.api.models.Conversation;
import com.example.api.models.User;
import com.example.api.repositories.UserRepository;
import com.example.api.services.Mercure.MercurePublisher;
import com.example.api.services.User.UserService;

@Controller
@RequestMapping("/users/{userId}/contacts")
public class UserContactController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/random")
    private ResponseEntity<Object> random(
        @PathVariable("userId") Integer userId
    ) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, null, "User not found");
        }

        List<User> contacts = userService.getRandomContactListFor(user.get());

        return ResponseHandler.generateResponse(HttpStatus.OK, "contacts", contacts);
    }

    @GetMapping
    public ResponseEntity<Object> index(
        @PathVariable("userId") Integer userId
    ) {
        try {
            List<User> contacts = userService.getContactList(userId);

            return ResponseHandler.generateResponse(HttpStatus.OK, "contacts", contacts);
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    @PostMapping("/{contactId}")
    public ResponseEntity<Object> store(
        @PathVariable("userId") Integer userId,
        @PathVariable("contactId") Integer contactId,
        @RequestHeader("MercureAuthorization") String mercureAuthorization
    ) {
        try {
            if (userId.equals(contactId)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot add yourself as a contact");
            }

            String mercureToken = mercureAuthorization.replace("Bearer ", "");

            List<User> contacts = userService.addContactList(userId, contactId);

            User user = userRepository.findById(userId).get();
            User contact = userRepository.findById(contactId).get();

            Conversation conversation = user.getConversations().stream()
                .filter(c -> c.getParticipants().contains(user) && c.getParticipants().contains(contact))
                .findFirst()
                .get();

            var mercurePublisher = new MercurePublisher("http://mercure/.well-known/mercure", mercureToken);

            var pingNewContactToAuthUser = mercurePublisher.create(
                "user.contact.created", 
                contact.toJson(),
                "/users/" + user.getUsername() + user.getId(),
                "contacts"
            );

            var pingNewContactToContactUser = mercurePublisher.create(
                "user.contact.created", 
                user.toJson(),
                "/users/" + contact.getUsername() + contact.getId(),
                "contacts"
            );

            var pingNewConversationToAuthUser = mercurePublisher.create(
                "user.conversation.created", 
                conversation.toJson(),
                "/users/" + user.getUsername() + user.getId(),
                "conversations"
            );

            var pingNewConversationToContactUser = mercurePublisher.create(
                "user.conversation.created", 
                conversation.toJson(),
                "/users/" + contact.getUsername() + contact.getId(),
                "conversations"
            );

            mercurePublisher.publish(pingNewContactToAuthUser);
            mercurePublisher.publish(pingNewConversationToAuthUser);
            mercurePublisher.publish(pingNewContactToContactUser);
            mercurePublisher.publish(pingNewConversationToContactUser);
            
            return ResponseHandler.generateResponse(HttpStatus.OK, "contacts", contacts);
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (UnauthorizedPublisherException|PublishRejectedException|HubNotFoundException e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    @DeleteMapping("/{contactId}")  
    public ResponseEntity<Object> delete(
        @PathVariable("contactId") Integer contactId,
        @PathVariable("userId") Integer userId,
        @RequestHeader("MercureAuthorization") String mercureAuthorization
    ) {  
        try {
            String mercureToken = mercureAuthorization.replace("Bearer ", "");

            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<User> optionalContact = userRepository.findById(contactId);
    
            if (optionalUser.isEmpty() || optionalContact.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User or contact not found");
            }
    
            User user = optionalUser.get();
            User contact = optionalContact.get();

            userService.deleteUserContact(user, contact);

            var mercurePublisher = new MercurePublisher("http://mercure/.well-known/mercure", mercureToken);

            var pingContact = mercurePublisher.create(
                "user.contact.deleted", 
                user.toJson(),
                "/users/" + contact.getUsername() + contact.getId(),
                "contacts"
            );

            var pingAuthUser = mercurePublisher.create(
               "user.contact.deleted", 
                contact.toJson(),
                "/users/" + user.getUsername() + user.getId(),
                "contacts"
            );

            mercurePublisher.publish(pingContact);
            mercurePublisher.publish(pingAuthUser);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException responseStatusException) {
            return ResponseHandler.generateResponse(responseStatusException, null, responseStatusException.getMessage());
        } catch (UnauthorizedPublisherException|PublishRejectedException|HubNotFoundException e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }  
}
