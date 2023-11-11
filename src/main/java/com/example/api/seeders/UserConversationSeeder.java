package com.example.api.seeders;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class UserConversationSeeder {
    private static JdbcTemplate jdbc;

    public static void seed(JdbcTemplate jdbcTemplate, List<Integer> userIds, String[] usernames) {
        jdbc = jdbcTemplate;
        Integer firstParticipantUserId = userIds.get(0);
        String firstParticipantUsername = jdbc.queryForObject("SELECT username FROM users WHERE id = " + firstParticipantUserId, String.class);
        String[] filteredUsernames = removeElement(firstParticipantUsername, usernames);

        for (int j = 0; j < userIds.size() - 1; j++) {
            String secondParticipantUsername = filteredUsernames[j];
            Integer secondParticipantUserId = jdbc.queryForObject("SELECT id FROM users WHERE username = '" + secondParticipantUsername + "'", Integer.class);
            String conversationName = firstParticipantUsername + " et " + secondParticipantUsername;

            jdbc.execute("INSERT INTO conversations (name, type) VALUES ('" + conversationName + "', 'PRIVATE')");

            Integer conversationId = jdbc.queryForObject("SELECT id FROM conversations WHERE name = '" + conversationName + "'", Integer.class);

            jdbc.execute("INSERT INTO users_conversations (user_id, conversation_id) VALUES (" + firstParticipantUserId + ", " + conversationId + ")");
            
            jdbc.execute("INSERT INTO users_conversations (user_id, conversation_id) VALUES (" + secondParticipantUserId + ", " + conversationId + ")");
        }
    }

    private static String[] removeElement(String firstParticipantUsername, String[] usernames) {
        String[] filteredUsernames = new String[usernames.length - 1];
        int indexToRemove = -1;

        for (int i = 0; i < usernames.length; i++) {
            if (firstParticipantUsername.equals(usernames[i])) {
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
