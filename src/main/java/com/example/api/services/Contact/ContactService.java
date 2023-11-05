package com.example.api.services.Contact;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.models.Contact;
import com.example.api.repositories.ContactRepository;
import com.example.api.validation.Contacts.ContactDTO;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        
        contactRepository.findAll().forEach(contacts::add);

        return contacts;
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
