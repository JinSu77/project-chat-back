package com.example.api.seeders.Conversation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.jdbc.core.JdbcTemplate;

import com.github.javafaker.Faker;

public class ConversationMessageSeeder {
    private JdbcTemplate jdbc;

    public ConversationMessageSeeder(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public void seed(Integer conversationId, Integer numberOfMessages, String[] messages) {
        Faker faker = new Faker();
        Random random = new Random();

        List<Integer> userIds = jdbc.queryForList("SELECT user_id FROM users_conversations WHERE conversation_id = " + conversationId, Integer.class);

        for (int i = 0; i < numberOfMessages; i++) {
            Integer randomInteger = random.nextInt(userIds.size());
            Integer userId = userIds.get(randomInteger);

            String username = jdbc.queryForObject("SELECT username FROM users WHERE id = " + userId, String.class);
            String content = messages[faker.random().nextInt(0, messages.length - 1)].replace("'", "''");

            Timestamp created_at = Timestamp.valueOf(LocalDateTime.now());

            jdbc.execute("INSERT INTO messages (content, conversation_id, created_at, user_id, username) VALUES " 
                + "('" 
                + content
                + "', '" 
                + conversationId
                + "', '" 
                + created_at
                + "', '" 
                + userId
                + "', '"
                + username
                + "')"
            );   
        }
    }
}
