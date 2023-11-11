package com.example.api.validation.Messages;

import org.hibernate.validator.constraints.Range;

import com.example.api.models.Channel;
import com.example.api.models.Message;

import jakarta.annotation.Nullable;
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
    
    public Message toMessage(
        @Nullable Integer conversation_id, 
        Integer user_id,
        @Nullable Channel channel
    ) {
        Message message = new Message();

        message.setContent(this.content);

        if (channel != null) {
            message.setChannel(channel);
        }

        if (conversation_id != null) message.setConversationId(conversation_id);
        
        message.setUserId(user_id);

        return message;
    }
}
