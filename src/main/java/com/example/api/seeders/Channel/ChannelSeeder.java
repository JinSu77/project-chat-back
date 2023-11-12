package com.example.api.seeders.Channel;

import org.springframework.jdbc.core.JdbcTemplate;

public class ChannelSeeder {
    public static void seed(JdbcTemplate jdbcTemplate) {
        Integer channelCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM channels", Integer.class);

        if (channelCount != 0) return;

        System.out.println("Seeding channels...");

        for (int i = 1; i <= 10; i++) {
            if (i == 10) {
                jdbcTemplate.execute("INSERT INTO channels (name) VALUES ('Channel0" + i + "')");
                return;
            }

            jdbcTemplate.execute("INSERT INTO channels (name) VALUES ('Channel00" + i + "')");
        }
    }
}
