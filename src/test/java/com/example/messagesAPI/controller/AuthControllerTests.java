package com.example.messagesAPI.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.messagesAPI.dto.auth.LoginRequest;
import com.example.messagesAPI.dto.auth.RegisterRequest;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.service.AuthService;
import com.example.messagesAPI.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @MockBean
    JWTService jwtService;

    @Test
    void Test_RegisterUser_Success() throws Exception {
        //Setup
        RegisterRequest registerRequest = new RegisterRequest("test","test","test","test");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(registerRequest);

        when(authService.registerUser(registerRequest.name(),registerRequest.lastname(),registerRequest.email(), registerRequest.password())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk());

        // Verify the service method was called
        verify(authService, times(1)).registerUser(registerRequest.name(),registerRequest.lastname(),registerRequest.email(), registerRequest.password());
    }

    @Test
    void Test_RegisterUser_Error() throws Exception {
        //Setup
        RegisterRequest registerRequest = new RegisterRequest("test","test","test","test");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(registerRequest);

        when(authService.registerUser(registerRequest.name(),registerRequest.lastname(),registerRequest.email(), registerRequest.password())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());

        // Verify the service method was called
        verify(authService, times(1)).registerUser(registerRequest.name(),registerRequest.lastname(),registerRequest.email(), registerRequest.password());
    }

    @Test
    void Test_Login_Success() throws Exception {
        //Setup
        LoginRequest loginRequest = new LoginRequest("test","test");
        String expectedValue = "TestToken";

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(loginRequest);

        when(authService.validateUser(loginRequest.email(),loginRequest.password())).thenReturn(true);
        when(jwtService.createJWT(loginRequest.email())).thenReturn(expectedValue);

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedValue));

        // Verify the service method was called
        verify(authService, times(1)).validateUser(loginRequest.email(),loginRequest.password());
        verify(jwtService, times(1)).createJWT(loginRequest.email());
    }

    @Test
    void Test_Login_Error() throws Exception {
        //Setup
        LoginRequest loginRequest = new LoginRequest("test","test");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(loginRequest);

        when(authService.validateUser(loginRequest.email(),loginRequest.password())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());

        // Verify the service method was called
        verify(authService, times(1)).validateUser(loginRequest.email(),loginRequest.password());
    }
}
