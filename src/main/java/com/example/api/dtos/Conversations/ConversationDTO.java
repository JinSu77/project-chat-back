package com.example.api.dtos.Conversations;

import java.util.List;

import com.example.api.enums.ConversationType;
import com.example.api.models.Conversation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Setter;

@Setter
public class ConversationDTO {
    @NotBlank(message = "Type is required")
    @Pattern(regexp = "PRIVATE|PUBLIC", message = "Type must be 'PRIVATE' or 'PUBLIC'")
    private String type;

    @NotEmpty(message = "Participants is required")
    private List<@Positive(message = "Participant ID must be a positive integer") Integer> participants;

    public Conversation toConversationWithoutParticipants() {
        Conversation conversation = new Conversation();

        conversation.setType(ConversationType.valueOf(this.type));

        return conversation;
    }

    public List<Integer> getParticipantIds() {
        return participants;
    }
}
