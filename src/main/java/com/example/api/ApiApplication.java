package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.seeders.ChannelSeeder;
import com.example.api.seeders.UserSeeder;

import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@RestController
public class ApiApplication {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final String environment = "development"; // TODO: create a config file

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void seed(ContextRefreshedEvent event) {	
		if (! environment.equals("development")) return;

		ChannelSeeder.seed(jdbcTemplate);

		UserSeeder.seed(jdbcTemplate);		
	}
}
