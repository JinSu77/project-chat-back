package com.example.api.seeders;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.JdbcTemplate;

import com.github.javafaker.Faker;

public class ChannelMessageSeeder {
    private static JdbcTemplate jdbc;
    private static Integer NUMBER_OF_MESSAGES_PER_USER = 10;

    public static void seed(JdbcTemplate jdbcTemplate, List<Integer> userIds) {
        Integer channelCounts = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM channels", Integer.class);

        if (channelCounts == 0) return;

        Faker faker = new Faker(Locale.FRANCE);
        jdbc = jdbcTemplate;

        List<Integer>  channelIds = jdbc.queryForList("SELECT id FROM channels", Integer.class);

        Integer totalMessages = userIds.size() * NUMBER_OF_MESSAGES_PER_USER;

        for (int i = 0; i < totalMessages; i++) {
            Integer channelIndex = faker.random().nextInt(0, channelIds.size() - 1);
            Integer userIndex = faker.random().nextInt(0, userIds.size() - 1);
            Integer channelId = channelIds.get(channelIndex);
            Integer userId = userIds.get(userIndex);
            String content = faker.lorem().sentence();
            Timestamp created_at = Timestamp.valueOf(LocalDateTime.now());

            jdbc.execute("INSERT INTO messages (channel_id, content, created_at, user_id) VALUES " 
                + "(" 
                + channelId 
                + ", '" 
                + content
                + "', '" 
                + created_at
                + "', '" 
                + userId
                + "')"
            );    
        }
    }
}
