package com.example.messagesAPI.controller;

import com.example.messagesAPI.dto.auth.RegisterRequest;
import com.example.messagesAPI.dto.message.SendMessageRequest;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.service.AuthService;
import com.example.messagesAPI.service.JWTService;
import com.example.messagesAPI.service.MessagesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessagesControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MessagesService messagesService;

    @Autowired
    JWTService jwtService;

    @Test
    void Test_GetMessages_Success() throws Exception {
        //Setup
        List<Message> messages = new ArrayList<>();
        String token = jwtService.createJWT("test");

        when(messagesService.getMessages()).thenReturn(messages);

        // Act & Assert
        mockMvc.perform(get("/messages")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Verify the service method was called
        verify(messagesService, times(1)).getMessages();
    }

    @Test
    void Test_GetMessages_Error() throws Exception {
        //Setup
        List<Message> messages = new ArrayList<>();
        String token = jwtService.createJWT("test");

        when(messagesService.getMessages()).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/messages")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());

        // Verify the service method was called
        verify(messagesService, times(1)).getMessages();
    }

    @Test
    void Test_SendMessage_Success() throws Exception {
        //Setup
        SendMessageRequest sendMessageRequest = new SendMessageRequest("test","test");
        String token = jwtService.createJWT("test");

        ObjectMapper objectMapper = new ObjectMapper();
        String messageJson = objectMapper.writeValueAsString(sendMessageRequest);

        when(messagesService.sendMessageToFriend(sendMessageRequest.content(),sendMessageRequest.receiver())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/messages/send")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageJson))
                .andExpect(status().isOk());

        // Verify the service method was called
        verify(messagesService, times(1)).sendMessageToFriend(sendMessageRequest.content(),sendMessageRequest.receiver());
    }

    @Test
    void Test_SendMessage_Error() throws Exception {
        //Setup
        SendMessageRequest sendMessageRequest = new SendMessageRequest("test","test");
        String token = jwtService.createJWT("test");

        ObjectMapper objectMapper = new ObjectMapper();
        String messageJson = objectMapper.writeValueAsString(sendMessageRequest);

        when(messagesService.sendMessageToFriend(sendMessageRequest.content(),sendMessageRequest.receiver())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/messages/send")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageJson))
                .andExpect(status().isBadRequest());

        // Verify the service method was called
        verify(messagesService, times(1)).sendMessageToFriend(sendMessageRequest.content(),sendMessageRequest.receiver());
    }

}
