package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.seeders.Channel.ChannelSeeder;
import com.example.api.seeders.User.UserSeeder;

import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@RestController
public class ApiApplication {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${application.seeder.demo.username}")
    private String DEMO_USERNAME;

    @Value("${application.seeder.demo.password}")
    private String DEMO_USERS_PASSWORD;

    @Value("${application.seeder.number-of-users}")
    private String NUMBER_OF_USERS;

	@Value("${application.seeder.channel.number-of-messages-per-user}")
    private String NUMBER_OF_CHANNEL_MESSAGES_PER_USER;

	@Value("${application.seeder.number-of-messages-per-conversation}")
	private String NUMBER_OF_MESSAGES_PER_CONVERSATION;

	@Value("${application.environment}")
	private String environment;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void seed(ContextRefreshedEvent event) {
		if (! environment.equals("development")) return;

		ChannelSeeder channelSeeder = new ChannelSeeder(jdbcTemplate);
		channelSeeder.seed();

		UserSeeder userSeeder = new UserSeeder(jdbcTemplate);
		userSeeder.seed(
			DEMO_USERNAME,
			DEMO_USERS_PASSWORD,
			Integer.parseInt(NUMBER_OF_USERS),
			Integer.parseInt(NUMBER_OF_CHANNEL_MESSAGES_PER_USER),
			Integer.parseInt(NUMBER_OF_MESSAGES_PER_CONVERSATION)
		);
	}
}
