package com.example.api.validation.Messages;

import java.util.Date;
import org.hibernate.validator.constraints.Range;

import com.example.api.models.Message;

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
    
    public Message toMessage() {
        Message message = new Message();

        message.setContent(this.content);

        message.setUserId(this.user_id);

        message.setCreatedAt(new Date());

        return message;
    }
}
