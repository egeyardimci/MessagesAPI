package com.example.messagesAPI.controller;

import com.example.messagesAPI.TestSocketIOConfig;
import com.example.messagesAPI.dto.message.GetMessageRequest;
import com.example.messagesAPI.dto.message.SendMessageRequest;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.service.JWTService;
import com.example.messagesAPI.service.MessagesService;
import com.example.messagesAPI.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessagesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MessagesControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MessagesService messagesService;

    @MockBean
    private UserService userService;

    @MockBean
    JWTService jwtService;

    @Test
    void Test_GetChatMessages_Success() throws Exception {
        //Setup
        ObjectId mockId = new ObjectId("507f1f77bcf86cd799439011");
        List<Message> messages = new ArrayList<>();
        String token = jwtService.createJWT("test");

        when(messagesService.getMessagesFromChat(mockId)).thenReturn(messages);

        GetMessageRequest getMessageRequest = new GetMessageRequest(mockId.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(getMessageRequest);

        // Act & Assert
        mockMvc.perform(post("/messages/get")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        // Verify the service method was called
        verify(messagesService, times(1)).getMessagesFromChat(mockId);
    }

    @Test
    void Test_GetChatMessages_Error() throws Exception {
        //Setup
        ObjectId mockId = new ObjectId("507f1f77bcf86cd799439011");
        List<Message> messages = new ArrayList<>();
        String token = jwtService.createJWT("test");

        when(messagesService.getMessagesFromChat(mockId)).thenReturn(null);

        GetMessageRequest getMessageRequest = new GetMessageRequest(mockId.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(getMessageRequest);

        // Act & Assert
        mockMvc.perform(post("/messages/get")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        // Verify the service method was called
        verify(messagesService, times(1)).getMessagesFromChat(mockId);
    }

}
