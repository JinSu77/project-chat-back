package com.example.api.seeders.User;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class UserConversationSeeder {
    private static JdbcTemplate jdbc;

    public static Integer[] seed(JdbcTemplate jdbcTemplate, List<Integer> userIds, String[] usernames) {
        System.out.println("Seeding users conversations...");

        jdbc = jdbcTemplate;
        Integer firstParticipantUserId = userIds.get(0);
        String firstParticipantUsername = jdbc.queryForObject("SELECT username FROM users WHERE id = " + firstParticipantUserId, String.class);

        System.out.println("####################################################################");
        System.out.println("###### Use this user to test the application");
        System.out.println("###### Username: " + firstParticipantUsername);
        System.out.println("####################################################################");

        String[] filteredUsernames = removeUsername(firstParticipantUsername, usernames);
        Integer[] conversationIds = new Integer[filteredUsernames.length];

        for (int j = 0; j < userIds.size() - 1; j++) {
            String secondParticipantUsername = filteredUsernames[j];
            Integer secondParticipantUserId = jdbc.queryForObject("SELECT id FROM users WHERE username = '" + secondParticipantUsername + "'", Integer.class);
            String conversationName = firstParticipantUsername + " et " + secondParticipantUsername;

            jdbc.execute("INSERT INTO conversations (name, type) VALUES ('" + conversationName + "', 'PRIVATE')");

            Integer conversationId = jdbc.queryForObject("SELECT id FROM conversations WHERE name = '" + conversationName + "'", Integer.class);

            conversationIds[j] = conversationId;

            jdbc.execute("INSERT INTO users_conversations (user_id, conversation_id) VALUES (" + firstParticipantUserId + ", " + conversationId + ")");
            
            jdbc.execute("INSERT INTO users_conversations (user_id, conversation_id) VALUES (" + secondParticipantUserId + ", " + conversationId + ")");
        }

        return conversationIds;
    }
    

    private static String[] removeUsername(String username, String[] usernames) {
        String[] filteredUsernames = new String[usernames.length - 1];
        int indexToRemove = -1;

        for (int i = 0; i < usernames.length; i++) {
            if (username.equals(usernames[i])) {
                indexToRemove = i;
                break;
            }
        }
        
        if (indexToRemove != -1) {
            for (int i = 0, j = 0; i < usernames.length; i++) {
                if (i != indexToRemove) {
                    filteredUsernames[j++] = usernames[i];
                }
            }
        }

        return filteredUsernames;
    }
}
