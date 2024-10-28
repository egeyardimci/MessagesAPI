package com.example.demo.configuration;


import com.example.demo.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // Disable CSRF for stateless APIs
            .cors(cors -> {})              // Enable CORS if needed, or configure with specifics
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set stateless session policy
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/login").permitAll()  // Public endpoint
                    .requestMatchers("/register").permitAll()  // Public endpoint
                    .requestMatchers("/messages/**").authenticated()
                    .requestMatchers("/friends/**").authenticated()// Secure endpoint
                    .anyRequest().authenticated() // Any other request requires authentication
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }
}