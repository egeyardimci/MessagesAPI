package com.example.messagesAPI.integration;

import com.example.messagesAPI.dto.auth.LoginRequest;
import com.example.messagesAPI.dto.auth.RegisterRequest;
import com.example.messagesAPI.dto.message.SendMessageRequest;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MessagesIntegrationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

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
    void TestIT_SendMessage_And_GetSentMessage() throws Exception {

        //Set up users
        User user1 = new User("test","test","test","test");
        User user2 = new User("test2","test2","test2","test2");

        userRepository.save(user1);
        userRepository.save(user2);

        user1 = userRepository.findByEmail(user1.getEmail());
        user2 = userRepository.findByEmail(user2.getEmail());

        user1.getFriends().add(user2.getId());
        user2.getFriends().add(user1.getId());

        userRepository.save(user1);
        userRepository.save(user2);

        ObjectMapper objectMapper = new ObjectMapper();

        LoginRequest loginRequest = new LoginRequest(user1.getEmail(),user1.getPassword());
        String loginUserJson = objectMapper.writeValueAsString(loginRequest);

        // Login to user
        MvcResult result = mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginUserJson))
                            .andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String token = JsonPath.read(responseBody, "$.token");

        //Send the test message
        SendMessageRequest messageRequest = new SendMessageRequest("test message",user2.getEmail());
        String messageJson = objectMapper.writeValueAsString(messageRequest);

        mockMvc.perform(post("/messages/send")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageJson))
                .andExpect(status().isOk());

        List<Message> messageList = new ArrayList<>();
        Message expectedMessage = new Message("test message", user1.getId(),user2.getId(),false);

        //See if the message is sent
        mockMvc.perform(get("/messages")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageList[0].content").value(expectedMessage.getContent()));
    }
}
