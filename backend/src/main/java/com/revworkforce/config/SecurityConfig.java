package com.revworkforce.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth

        // Public endpoints (login / future auth APIs)
        .requestMatchers("/api/auth/**").permitAll()

        // Admin APIs secured
        .requestMatchers("/api/admin/**").hasRole("ADMIN")

        // Employee + Manager features
        .requestMatchers("/api/leave/**").hasAnyRole("EMPLOYEE", "MANAGER")
        .requestMatchers("/api/performance/**").hasAnyRole("EMPLOYEE", "MANAGER")

        // All other APIs require authentication
        .anyRequest().authenticated()
      )
      // Modern non-deprecated way
      .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
