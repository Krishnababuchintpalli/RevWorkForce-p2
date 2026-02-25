package com.revworkforce.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
      // Disable CSRF for stateless REST APIs
      .csrf(csrf -> csrf.disable())

      // Stateless session (important for future JWT integration)
      .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

      // Authorization rules
      .authorizeHttpRequests(auth -> auth

        // Admin APIs
        .requestMatchers("/api/admin/**").hasRole("ADMIN")

        // Manager APIs
        .requestMatchers("/api/manager/**").hasRole("MANAGER")

        // Employee APIs
        .requestMatchers("/api/employee/**").hasRole("EMPLOYEE")

        // Public endpoints (if needed later like login)
        .requestMatchers("/api/auth/**").permitAll()

        // Everything else secured
        .anyRequest().authenticated()
      )

      // Using HTTP Basic temporarily (can switch to JWT later)
      .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
