package com.example.api.validation.Contacts;

import org.hibernate.validator.constraints.Range;

import com.example.api.models.Contact;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Email is required")
    private String email;

    @Range(min = 1, message = "User id is required")
    private Integer user_id;

    public Contact toContact() {
        Contact contact = new Contact();

        contact.setUsername(this.username);

        contact.setLastName(this.lastName);

        contact.setFirstName(this.firstName);

        contact.setEmail(this.email);

        contact.setUserId(this.user_id);

        return contact;
    }
}
