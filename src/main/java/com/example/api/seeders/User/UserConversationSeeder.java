package com.example.api.seeders.User;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public class UserConversationSeeder {
    private static JdbcTemplate jdbc;

    public static Integer[] seed(JdbcTemplate jdbcTemplate, List<Integer> userIds, String[] usernames) {
        System.out.println("Seeding users conversations and contacts...");

        jdbc = jdbcTemplate;
        Integer firstParticipantUserId = userIds.get(0);
        String firstParticipantUsername = jdbc.queryForObject("SELECT username FROM users WHERE id = " + firstParticipantUserId, String.class);
        String[] filteredUsernames = removeElementFromArray(firstParticipantUsername, usernames);
        Integer[] conversationIds = new Integer[filteredUsernames.length];

        for (int i = 0; i < userIds.size() - 1; i++) {
            String secondParticipantUsername = filteredUsernames[i];
            Integer secondParticipantUserId = jdbc.queryForObject("SELECT id FROM users WHERE username = '" + secondParticipantUsername + "'", Integer.class);

            jdbc.execute("INSERT INTO conversations (type) VALUES ('PRIVATE')");

            Integer conversationId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            conversationIds[i] = conversationId;

            jdbc.execute("INSERT INTO users_conversations (user_id, conversation_id) VALUES (" + firstParticipantUserId + ", " + conversationId + ")");
            jdbc.execute("INSERT INTO users_conversations (user_id, conversation_id) VALUES (" + secondParticipantUserId + ", " + conversationId + ")");
            
            jdbc.execute("INSERT INTO user_contacts (user_id, contact_id) VALUES (" + firstParticipantUserId + ", " + secondParticipantUserId + ")");
            jdbc.execute("INSERT INTO user_contacts (user_id, contact_id) VALUES (" + secondParticipantUserId + ", " + firstParticipantUserId + ")");
        }

        return conversationIds;
    }
    

    private static String[] removeElementFromArray(String username, String[] usernames) {
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
