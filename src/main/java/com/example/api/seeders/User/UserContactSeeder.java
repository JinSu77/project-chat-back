package com.example.api.seeders.User;

import org.springframework.jdbc.core.JdbcTemplate;

import com.example.api.models.User;

public class UserContactSeeder {
    private static JdbcTemplate jdbc;

    public static void seed(JdbcTemplate jdbcTemplate, Integer userId) {
        jdbc = jdbcTemplate;

        User userInfo = jdbc.queryForObject("SELECT * FROM users WHERE id = " + userId, (rs, rowNum) -> {
            User user = new User();

            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setUsername(rs.getString("username"));

            return user;
        });

        jdbc.execute("INSERT INTO contacts (username, last_name, first_name, email, user_id) VALUES "
            + "('"
            + userInfo.getUsername()
            + "', '"
            + userInfo.getLastName()
            + "', '"
            + userInfo.getFirstName()
            + "', '"
            + userInfo.getEmail()
            + "', "
            + userInfo.getId()
            + ")"
        );

        Integer contactId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        jdbc.execute("INSERT INTO users_contacts (contact_id, user_id) VALUES (" + contactId + ", " + userId + ")");
    }
}
