package com.example.api.services.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.models.Contact;
import com.example.api.models.User;
import com.example.api.repositories.ContactRepository;
import com.example.api.repositories.UserRepository;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.validation.Contacts.ContactDTO;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;
    private JwtUtil jwtUtil;
    UserRepository userRepository;

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        
        contactRepository.findAll().forEach(contacts::add);

        return contacts;
    }

    public List<Contact> getRandomContacts(String token) {

        String authenticatedUsername = jwtUtil.getAuthUsername(token);

        User user = userRepository.findByUsername(authenticatedUsername);
    
        List<Contact> randomContacts = new ArrayList<>();
        Random random = new Random();
        int count = 5;
    
        List<Contact> allContacts = (List<Contact>) contactRepository.findAll();
    
        while (randomContacts.size() < count) {
            int randomIndex = random.nextInt(allContacts.size());
            Contact randomContact = allContacts.get(randomIndex);
    
            if (!randomContact.getUsername().equals(user.getUsername()) && !randomContacts.contains(randomContact)) {
                randomContacts.add(randomContact);
            }
        }
        return randomContacts;
    }

    //SANS JWT MARCHE MAIS GET L'USER
    // public List<Contact> getRandomContacts() {

    //     List<Contact> randomContacts = new ArrayList<>();
    //     Random random = new Random();
    //     int count = 5;
    
    //     List<Contact> allContacts = (List<Contact>) contactRepository.findAll();
    
    //     while (randomContacts.size() < count) {
    //         int randomIndex = random.nextInt(allContacts.size());
    //         Contact randomContact = allContacts.get(randomIndex);
    
    //         if (!randomContacts.contains(randomContact)) {
    //             randomContacts.add(randomContact);
    //         }
    //     }
    //     return randomContacts;
    // }
    
    public Contact getContactById(int id)   
    {
        return contactRepository.findById(id).get();
    }

    public void delete(int id)   
    {  
        Contact contact = contactRepository.findById(id).get();

        contactRepository.delete(contact);
    }

    public Contact saveContact(ContactDTO contactDTO) {
        Contact contact = contactDTO.toContact();

        return contactRepository.save(contact);
    }
}
