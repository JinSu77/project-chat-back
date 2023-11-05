package com.example.api.validation.Conversations;

import java.util.List;

import com.example.api.enums.ConversationType;
import com.example.api.models.Conversation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Setter;

@Setter
public class ConversationDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    private String name;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "PRIVATE|PUBLIC", message = "Type must be 'PRIVATE' or 'PUBLIC'")
    private String type;

    @NotEmpty(message = "Participants is required")
    private List<@Positive(message = "Participant ID must be a positive integer") Integer> participants;

    public Conversation toConversationWithoutParticipants() {
        Conversation conversation = new Conversation();

        conversation.setName(name);
        conversation.setType(ConversationType.valueOf(this.type));

        return conversation;
    }

    public List<Integer> getParticipantIds() {
        return participants;
    }
}
