package com.example.api.models;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "contacts")
@Getter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer user_id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name="users_contacts",
        joinColumns = @JoinColumn(name="CONTACT_ID", referencedColumnName="ID"),
        inverseJoinColumns = @JoinColumn(name="USER_ID", referencedColumnName="ID")
    )
    @JsonIgnoreProperties(value = "contacts")
    private List<User> users = new ArrayList<User>();

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }
}
