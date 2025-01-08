package com.example.messagesAPI.integration;

import com.example.messagesAPI.TestSocketIOConfig;
import com.example.messagesAPI.dto.auth.LoginRequest;
import com.example.messagesAPI.dto.auth.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestSocketIOConfig.class)
public class AuthIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoTemplate mongoTemplate; // Add this for cleanup

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        // Clean database before each test
        if (mongoTemplate != null) {
            mongoTemplate.getDb().drop();
        }
    }

    @AfterAll
    static void cleanup() {
        mongoDBContainer.stop();
    }

    @Test
    void TestIT_CreateUser_Login() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest("test","test","test","test");

        ObjectMapper objectMapper = new ObjectMapper();
        String registerUserJson = objectMapper.writeValueAsString(registerRequest);

        // Create a test user
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerUserJson))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest("test","test");
        String loginUserJson = objectMapper.writeValueAsString(loginRequest);

        // Login to user
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginUserJson))
                .andExpect(status().isOk());

    }

}
