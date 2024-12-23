package com.example.messagesAPI.configuration;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.service.JWTService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.security.core.Authentication;
import com.corundumstudio.socketio.Configuration;

@org.springframework.context.annotation.Configuration
@Slf4j
public class SocketIOConfig {

    private String host = "localhost";
    private Integer port = 9092;

    private final JWTService jwtService;

    public SocketIOConfig(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setOrigin("*");

        // Add authentication listener
        config.setAuthorizationListener(handshakeData -> {
            try {
                // Get JWT from handshake query parameters
                String token = handshakeData.getSingleUrlParam("token");
                if (token == null) {
                    log.error("No JWT token provided");
                    return false;
                }

                // Validate JWT token
                Claims claims = jwtService.validateToken(token);
                if (claims == null) {
                    log.error("Invalid JWT token");
                    return false;
                }

                // Store user information in handshake data
                handshakeData.getHttpHeaders().set("email", claims.getSubject());
                System.out.println(claims.getSubject());

                return true;
            } catch (Exception e) {
                log.error("Authentication error: {}", e.getMessage());
                return false;
            }
        });

        return new SocketIOServer(config);
    }
}