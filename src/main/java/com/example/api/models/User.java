package com.example.api.models;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.lang.String;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
    
    public HashMap<String, Object> toArray() {
        HashMap<String, Object> user = new HashMap<String, Object>();

        user.put("username", username);
        user.put("lastName", lastName);
        user.put("firstName", firstName);
        user.put("email", email);

        return user;
    }

    public String getId() {
        return id.toString();
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public void setEmail(String email) {
        this.email = email; 
    }
}
