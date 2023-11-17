package com.example.api.seeders.User;

import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.api.seeders.Channel.ChannelMessageSeeder;
import com.example.api.seeders.Conversation.ConversationMessageSeeder;
import com.github.javafaker.Faker;

public class UserSeeder {
    private static JdbcTemplate jdbc;
    private static PasswordEncoder passwordEncoder;
    private static final String USER_PASSWORD = "password";
    private static final Integer NUMBER_OF_USERS = 10;
    
    public static void seed(JdbcTemplate jdbcTemplate) {
        Integer usersCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);

        if (usersCount != 0) return;

        Faker faker = new Faker(Locale.FRANCE);
        jdbc = jdbcTemplate;
        passwordEncoder = new BCryptPasswordEncoder();

        System.out.println("Seeding users...");

        Integer roleId = getUserRoleId();
        String[] emails = new String[NUMBER_OF_USERS];
        String[] usernames = new String[NUMBER_OF_USERS];

        for (int i = 0; i < NUMBER_OF_USERS; i++) {
            String email = faker.internet().emailAddress();
            String username = faker.name().username();

            for (int j = 0; j < NUMBER_OF_USERS; j++) {
                if (emails[j] == email) {
                    email = faker.internet().emailAddress();
                }

                if (usernames[j] == username) {
                    username = faker.name().username();
                }
            }

            emails[i] = email;
            usernames[i] = username;

            jdbc.execute("INSERT INTO users (email, first_name, last_name, password, username) VALUES" 
                + "('" + email
                + "', '"
                + faker.name().firstName() 
                + "', '" 
                + faker.name().lastName() 
                + "', '" 
                + passwordEncoder.encode(USER_PASSWORD) 
                + "', '" 
                +  username
                + "')"
            );

            addUserRole(email, roleId);

            // TODO: Créer un seeder UserContact 
            // Integer userId = jdbc.queryForObject("SELECT id FROM users WHERE email = '" + email + "'", Integer.class);
            // UserContactSeeder.seed(jdbc, userId);
        }
        
        List<Integer> userIds = jdbc.queryForList("SELECT id FROM users WHERE email in ('" + String.join("', '", emails) + "')", Integer.class);

        ChannelMessageSeeder.seed(jdbc, userIds);

        if (userIds.size() >= 2) {
            Integer[] conversationIds = UserConversationSeeder.seed(jdbc, userIds, usernames);
            
            System.out.println("Seeding conversation messages...");

            for (Integer conversationId : conversationIds) {
                ConversationMessageSeeder.seed(jdbc, conversationId);
            }            
        }
    }

    private static Integer getUserRoleId() {
        Boolean checkIfRoleExist = jdbc.queryForObject("SELECT EXISTS(SELECT 1 FROM roles WHERE name = 'USER')", Boolean.class);

        if (! checkIfRoleExist) {
            jdbc.execute("INSERT INTO roles (name) VALUES ('USER')");
        }

        return jdbc.queryForObject("SELECT id FROM roles WHERE name = 'USER'", Integer.class);
    }

    private static void addUserRole(String userEmail, Integer roleId) {
        Integer userId = jdbc.queryForObject("SELECT id FROM users WHERE email = '" + userEmail + "'", Integer.class);

        jdbc.execute("INSERT INTO users_roles (user_id, role_id) VALUES (" + userId + ", " + roleId + ")");
    }
}
