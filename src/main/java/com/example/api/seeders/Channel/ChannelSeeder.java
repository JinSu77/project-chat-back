package com.example.api.seeders.Channel;

import org.springframework.jdbc.core.JdbcTemplate;

public class ChannelSeeder {
    private JdbcTemplate jdbc;

    public ChannelSeeder(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public void seed() {
        Integer channelCount = jdbc.queryForObject("SELECT COUNT(*) FROM channels", Integer.class);

        if (channelCount != 0) return;

        System.out.println("Seeding channels...");

        for (int i = 1; i <= 10; i++) {
            if (i == 10) {
                jdbc.execute("INSERT INTO channels (name) VALUES ('Channel0" + i + "')");
                return;
            }

            jdbc.execute("INSERT INTO channels (name) VALUES ('Channel00" + i + "')");
        }
    }
}
