package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@RestController
public class ApiApplication {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void seed(ContextRefreshedEvent event) {		
		seedChannels();
	}

	private void seedChannels() {
		Integer channels = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM channels", Integer.class);

		if (channels == 0) {
			System.out.println("Seeding database");

			for (int i = 1; i <= 10; i++) {
				if (i == 10) {
					jdbcTemplate.execute("INSERT INTO channels (name) VALUES ('Channel0" + i + "')");
					return;
				}

				jdbcTemplate.execute("INSERT INTO channels (name) VALUES ('Channel00" + i + "')");
			}
		}
	}
}
