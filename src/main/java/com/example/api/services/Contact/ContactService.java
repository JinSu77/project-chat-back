package com.example.api.services.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.dtos.Contacts.ContactDTO;
import com.example.api.models.Contact;
import com.example.api.repositories.ContactRepository;
import com.example.api.services.Auth.JwtUtil;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        
        contactRepository.findAll().forEach(contacts::add);

        return contacts;
    }

    public List<Contact> getRandomContacts(String token) {
        String authenticatedUsername = jwtUtil.getAuthUsername(token);
        
        List<Contact> randomContacts = new ArrayList<>();

        String query = "SELECT * FROM contacts WHERE username != '" + authenticatedUsername + "' ORDER BY RAND() LIMIT 5";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        for (Map<String, Object> row : rows) {
            Contact contact = contactRepository.findById((Integer) row.get("id")).get();

            randomContacts.add(contact);
        }

        return randomContacts;
    }
    
    public Contact getContactById(Integer id)   
    {
        Optional <Contact> contact = contactRepository.findById(id);

        if (contact.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }

        return contact.get();
    }

    public void delete(int id)   
    {  
        Optional <Contact> contact = contactRepository.findById(id);

        if (contact.isEmpty()) {
            throw new ResponseStatusException(null, "Contact not found");
        }

        contactRepository.delete(contact.get());
    }

    public Contact saveContact(ContactDTO contactDTO) {
        Contact contact = contactDTO.toContact();

        return contactRepository.save(contact);
    }
}
