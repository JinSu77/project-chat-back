package com.example.api.validation.Contacts;

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

    public Contact toContact() {
        Contact contact = new Contact();

        contact.setUsername(this.username);

        contact.setLastName(this.lastName);

        contact.setFirstName(this.firstName);

        contact.setEmail(this.email);

        return contact;
    }
}
