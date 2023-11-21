package com.example.api.services.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.api.models.Contact;
import com.example.api.repositories.ContactRepository;
import com.example.api.services.Auth.JwtUtil;
import com.example.api.validation.Contacts.ContactDTO;

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
