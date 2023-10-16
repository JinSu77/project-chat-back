package com.example.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorizeRequests -> 
                authorizeRequests
                    .requestMatchers(
                        "/users/register", 
                        "/auth/login",
                        "/auth/me",
                        "/auth/logout"
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
                );
            

        return http.build();
    }
}