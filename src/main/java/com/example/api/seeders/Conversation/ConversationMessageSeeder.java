package com.example.api.seeders.Conversation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.jdbc.core.JdbcTemplate;

import com.github.javafaker.Faker;

public class ConversationMessageSeeder {
        private static JdbcTemplate jdbc;
        private static final Integer NUMBER_OF_MESSAGES = 5;

    public static void seed(JdbcTemplate jdbcTemplate, Integer conversationId) {
        jdbc = jdbcTemplate;
        Faker faker = new Faker();
        Random random = new Random();

        List<Integer> userIds = jdbc.queryForList("SELECT user_id FROM users_conversations WHERE conversation_id = " + conversationId, Integer.class);

        for (int i = 0; i < NUMBER_OF_MESSAGES; i++) {
            Integer randomInteger = random.nextInt(userIds.size());
            Integer userId = userIds.get(randomInteger);
            String content = faker.lorem().sentence();
            Timestamp created_at = Timestamp.valueOf(LocalDateTime.now());

            jdbc.execute("INSERT INTO messages (content, conversation_id, created_at, user_id) VALUES " 
                + "('" 
                + content 
                + "', '" 
                + conversationId
                + "', '" 
                + created_at
                + "', '" 
                + userId
                + "')"
            );   
        }
    }
}
