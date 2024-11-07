package com.example.messagesAPI.service;

import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTests {

    @Mock
    UserService userService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Test_RegisterUser_Success(){
        //Create mock data
        User user = new User("test","test","test","test");

        // Simulate successful save
        when(userService.save(user)).thenReturn(true);
        boolean result = authService.registerUser(user.getName(),user.getLastName(),user.getEmail(),user.getPassword());

        //Check result
        verify(userService, times(1)).save(user);
        assertTrue(result);
    }

    @Test
    void Test_ValidateUser_Success(){
        //Create mock data
        User user = new User("test","test","test","test");

        // Simulate successful save
        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        boolean result = authService.validateUser(user.getEmail(),user.getPassword());

        //Check result
        verify(userService, times(1)).findByEmail(user.getEmail());
        assertTrue(result);
    }

    @Test
    void Test_ValidateUser_Error(){
        //Create mock data
        User user = new User("test","test","test","test");

        // Simulate successful save
        when(userService.findByEmail(user.getEmail())).thenReturn(null);
        boolean result = authService.validateUser(user.getEmail(),user.getPassword());

        //Check result
        verify(userService, times(1)).findByEmail(user.getEmail());
        assertFalse(result);
    }

}
