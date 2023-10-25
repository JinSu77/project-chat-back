package com.example.api.validation.Messages;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    @NotBlank(message = "Content is required")
    private String content;

    @Range(min = 1, message = "User id is required")
    private Integer user_id;
}
