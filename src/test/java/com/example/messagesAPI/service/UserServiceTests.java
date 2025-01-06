package com.example.messagesAPI.service;

import com.example.messagesAPI.model.User;
import com.example.messagesAPI.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Test_SaveUser_Success(){
        //Create a user
        User user = new User("test","test","test","test");

        // Simulate successful save
        when(userRepository.save(user)).thenReturn(user);
        boolean result = userService.save(user);

        //Check result
        assertTrue(result, "The save method should return true on successful save");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void Test_SaveUser_Error(){
        //Create a user
        User user = new User("test","test","test","test");

        // Simulate an exception during save
        when(userRepository.save(user)).thenThrow(new RuntimeException("Database error"));
        boolean result = userService.save(user);

        //Check result
        assertFalse(result, "The save method should return false if an exception occurs");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void Test_FindByEmail_Success(){
        //Create a user
        String expectedEmail = "test";
        User user = new User("test","test",expectedEmail,"test");


        // Simulate
        when(userRepository.findByEmail(expectedEmail)).thenReturn(user);
        User result = userService.findByEmail(expectedEmail);

        //Check result
        assertNotNull(result, "User should not be null");
        assertEquals(expectedEmail, result.getEmail(), "Email should match the expected value");
        verify(userRepository, times(1)).findByEmail(expectedEmail);

    }

    @Test
    void Test_FindByEmail_Error(){
        //Create a user
        String expectedEmail = "test";
        User user = new User("test","test",expectedEmail,"test");

        // Simulate an exception
        when(userRepository.findByEmail(expectedEmail)).thenThrow(new RuntimeException("Database error"));
        User result = userService.findByEmail(expectedEmail);

        //Check result
        assertNull(result, "The save method should return false if an exception occurs");
        verify(userRepository, times(1)).findByEmail(expectedEmail);

    }

    @Test
    void Test_FindById_Success(){
        //Create a user
        User user = new User("test","test","test","test");
        ObjectId expectedId = user.getId();

        // Simulate successful save
        when(userRepository.findById(expectedId)).thenReturn(user);
        User result = userService.findById(expectedId);

        //Check result
        assertNotNull(result, "User should not be null");
        assertEquals(expectedId, result.getId(), "Email should match the expected value");
        verify(userRepository, times(1)).findById(expectedId);

    }

    @Test
    void Test_FindById_Error(){
        //Create a user
        User user = new User("test","test","test","test");
        ObjectId expectedId = user.getId();

        // Simulate successful save
        when(userRepository.findById(expectedId)).thenThrow(new RuntimeException("Database error"));
        User result = userService.findById(expectedId);

        //Check result
        assertNull(result, "The save method should return false if an exception occurs");
        verify(userRepository, times(1)).findById(expectedId);
    }

}
