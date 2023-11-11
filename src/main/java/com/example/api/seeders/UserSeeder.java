package com.example.api.seeders;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserSeeder {
    private static JdbcTemplate jdbc;
    private static PasswordEncoder passwordEncoder;

    private static final String INSERT_USER_QUERY =
    "INSERT INTO users (email, first_name, last_name, password, username) VALUES " +
    "('jean.dupont@example.com', 'Jean', 'Dupont', 'password', 'jeandupont')," +
    "('alice.martin@example.com', 'Alice', 'Martin', 'password', 'alicemartin')," +
    "('pierre.leroux@example.com', 'Pierre', 'Leroux', 'password', 'pierreleroux')," +
    "('emilie.blanc@example.com', 'Émilie', 'Blanc', 'password', 'emilieblanc')," +
    "('michel.bernard@example.com', 'Michel', 'Bernard', 'password', 'michelbernard')," +
    "('sophie.dubois@example.com', 'Sophie', 'Dubois', 'password', 'sophiedubois')," +
    "('antoine.petit@example.com', 'Antoine', 'Petit', 'password', 'antoinepetit')," +
    "('isabelle.marchand@example.com', 'Isabelle', 'Lefèvre', 'password', 'isabellemarchand')," +
    "('thomas.robert@example.com', 'Thomas', 'Robert', 'password', 'thomasrobert')," +
    "('lucie.girard@example.com', 'Lucie', 'Girard', 'password', 'luciegirard')";

    private static final String[] USER_EMAILS = {
        "jean.dupont@example.com",
        "alice.martin@example.com",
        "pierre.leroux@example.com",
        "emilie.blanc@example.com",
        "michel.bernard@example.com",
        "sophie.dubois@example.com",
        "antoine.petit@example.com",
        "isabelle.marchand@example.com",
        "thomas.robert@example.com",
        "lucie.girard@example.com"
    };

    private static final String USER_PASSWORD = "password";
    
    public static void seed(JdbcTemplate jdbcTemplate) {
        Integer usersCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);

        System.out.println("usersCount: " + usersCount);

        if (usersCount != 0) return;

        System.out.println("Seeding users...");

        jdbc = jdbcTemplate;

        passwordEncoder = new BCryptPasswordEncoder();

        jdbc.execute(INSERT_USER_QUERY);

        usersCount = jdbc.queryForObject("SELECT COUNT(*) FROM users", Integer.class);

        Integer roleId = getUserRoleId();

        for(int i = 0; i < usersCount; i++) {
            updateUserPassword(USER_EMAILS[i]);

            addUserRole(USER_EMAILS[i], roleId);

            // TODO: addChannel(USER_EMAILS[i]);
        }
    }

    private static void updateUserPassword(String userEmail) {
        jdbc.update(
            "UPDATE users SET password = ? WHERE email = ?",
            passwordEncoder.encode(USER_PASSWORD),
            userEmail
        );
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
