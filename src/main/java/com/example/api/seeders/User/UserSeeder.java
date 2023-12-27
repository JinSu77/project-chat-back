package com.example.api.seeders.User;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.api.seeders.Channel.ChannelMessageSeeder;
import com.example.api.seeders.Conversation.ConversationMessageSeeder;
import com.github.javafaker.Faker;

public class UserSeeder {
    private JdbcTemplate jdbc;

    private static PasswordEncoder passwordEncoder;

    @Value("${application.seeder.demo.username}")
    private String DEMO_USERNAME;

    @Value("${application.seeder.demo.password}")
    private String USERS_PASSWORD;

    @Value("${application.seeder.number-of-users}")
    private String numberOfUsers;

    private String[] messages = {
        "Salut, comment ça va ?",
        "Qu'est-ce que tu fais en ce moment ?",
        "J'ai passé une excellente journée!",
        "Tu as des projets pour le week-end ?",
        "Comment s'est passée ta journée ?",
        "C'est super de te parler !",
        "Je suis en train de regarder un film.",
        "As-tu entendu parler de la dernière nouvelle ?",
        "Quel temps fait-il chez toi ?",
        "Je pense que je vais prendre une pause café.",
        "On pourrait se retrouver plus tard ?",
        "Comment va ta famille ?",
        "J'adore cette nouvelle application de messagerie!",
        "Que penses-tu de cette idée ?",
        "Quels sont tes hobbies préférés ?",
        "Tu veux sortir ce soir ?",
        "Comment s'est passée ta semaine ?",
        "Quel est ton plat préféré ?",
        "Je suis en train de lire un livre passionnant.",
        "Où est-ce que tu travailles actuellement ?",
        "Quel genre de musique préfères-tu écouter ?",
        "As-tu des projets de voyage pour cette année ?",
        "J'ai entendu dire que le nouveau restaurant en ville est génial!",
        "Quelle est ta série TV préférée en ce moment ?",
        "Si tu pouvais voyager dans le temps, où irais-tu ?",
        "Je suis curieux, quel est ton animal de compagnie préféré ?",
        "Quel est le dernier film que tu as vu au cinéma ?",
        "Tu préfères le café ou le thé le matin ?",
        "Si tu pouvais avoir un super pouvoir, lequel choisirais-tu ?",
        "Quelle belle journée ensoleillée!",
        "Je viens de terminer un projet passionnant au travail.",
        "La vie est pleine de surprises!",
        "J'apprécie vraiment le temps que nous passons ensemble.",
        "Parfois, il est bon de simplement se détendre et ne rien faire.",
        "Je suis reconnaissant(e) pour les petites joies de la vie.",
        "La persévérance est la clé du succès!",
        "J'ai découvert une nouvelle recette délicieuse à essayer.",
        "La créativité rend la vie plus intéressante.",
        "Chaque jour est une nouvelle opportunité.",
        "Les rêves sont le carburant de l'imagination.",
        "La gentillesse fait du monde un endroit meilleur.",
        "Je crois en toi et en ton potentiel.",
        "Les petits moments font les grands souvenirs.",
        "Le travail d'équipe rend les rêves possibles.",
        "La simplicité apporte une beauté authentique.",
        "La gratitude transforme ce que nous avons en suffisance."
    };

    public UserSeeder(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }
    
    public void seed(
        String demoUsername,
        String demoUsersPassword,
        Integer numberOfUsers,
        Integer numberOfChannelMessagesPerUser,
        Integer numberOfConversationMessages
    ) {
        Integer usersCount = jdbc.queryForObject("SELECT COUNT(*) FROM users", Integer.class);

        if (usersCount != 0) return;

        Faker faker = new Faker(Locale.FRANCE);
        passwordEncoder = new BCryptPasswordEncoder();

        System.out.println("Seeding users...");

        Integer roleId = getUserRoleId();
        String[] emails = new String[numberOfUsers];
        String[] usernames = new String[numberOfUsers];

        for (int i = 0; i < numberOfUsers; i++) {
            String email = faker.internet().emailAddress();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            String username = (i != 0) 
                ? firstName.substring(0, 3) + lastName.substring(0, 3)
                : demoUsername;

            for (int j = 0; j < numberOfUsers; j++) {
                if (emails[j] == email) {
                    email = faker.internet().emailAddress();
                }

                if (usernames[j] == username) {
                    username += faker.random().nextInt(0, 100);
                }
            }

            emails[i] = email;
            usernames[i] = username;

            jdbc.execute("INSERT INTO users (email, first_name, last_name, password, username) VALUES" 
                + "('" + email
                + "', '"
                + firstName
                + "', '" 
                + lastName
                + "', '" 
                + passwordEncoder.encode(demoUsersPassword) 
                + "', '" 
                +  username
                + "')"
            );

            addUserRole(email, roleId);
        }
        
        List<Integer> userIds = jdbc.queryForList("SELECT id FROM users WHERE email in ('" + String.join("', '", emails) + "') ORDER BY id", Integer.class);

        ChannelMessageSeeder channelMessageSeeder = new ChannelMessageSeeder(jdbc);
        
        channelMessageSeeder.seed(userIds, numberOfChannelMessagesPerUser, messages);

        if (userIds.size() >= 2) {
            Integer[] conversationIds = UserConversationSeeder.seed(jdbc, userIds, usernames);
            
            System.out.println("Seeding conversation messages...");

            ConversationMessageSeeder conversationMessageSeeder = new ConversationMessageSeeder(jdbc);

            for (Integer conversationId : conversationIds) {
                conversationMessageSeeder.seed(conversationId, numberOfConversationMessages, messages);
            }            
        }
    }

    private Integer getUserRoleId() {
        Boolean checkIfRoleExist = jdbc.queryForObject("SELECT EXISTS(SELECT 1 FROM roles WHERE name = 'USER')", Boolean.class);

        if (! checkIfRoleExist) {
            jdbc.execute("INSERT INTO roles (name) VALUES ('USER')");
        }

        return jdbc.queryForObject("SELECT id FROM roles WHERE name = 'USER'", Integer.class);
    }

    private void addUserRole(String userEmail, Integer roleId) {
        Integer userId = jdbc.queryForObject("SELECT id FROM users WHERE email = '" + userEmail + "'", Integer.class);

        jdbc.execute("INSERT INTO users_roles (user_id, role_id) VALUES (" + userId + ", " + roleId + ")");
    }
}
