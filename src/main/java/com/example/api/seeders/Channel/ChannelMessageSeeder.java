package com.example.api.seeders.Channel;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.JdbcTemplate;

import com.github.javafaker.Faker;

public class ChannelMessageSeeder {
    private JdbcTemplate jdbc;

    public ChannelMessageSeeder(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public void seed(List<Integer> userIds, Integer numberOfMessagesPerUser, String[] messages) {
        Integer channelCounts = jdbc.queryForObject("SELECT COUNT(*) FROM channels", Integer.class);

        if (channelCounts == 0) return;

        System.out.println("Seeding channel messages...");

        Faker faker = new Faker(Locale.FRANCE);

        List<Integer>  channelIds = jdbc.queryForList("SELECT id FROM channels", Integer.class);

        Integer totalMessages = userIds.size() * numberOfMessagesPerUser;

        for (int i = 0; i < totalMessages; i++) {
            Integer channelIndex = faker.random().nextInt(0, channelIds.size() - 1);
            Integer userIndex = faker.random().nextInt(0, userIds.size() - 1);
            Integer channelId = channelIds.get(channelIndex);
            Integer userId = userIds.get(userIndex);
            
            String username = jdbc.queryForObject("SELECT username FROM users WHERE id = " + userId, String.class);
            String content = messages[faker.random().nextInt(0, messages.length - 1)].replace("'", "''");

            Timestamp created_at = Timestamp.valueOf(LocalDateTime.now());

            jdbc.execute("INSERT INTO messages (channel_id, content, created_at, user_id, username) VALUES " 
                + "(" 
                + channelId 
                + ", '" 
                + content
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
