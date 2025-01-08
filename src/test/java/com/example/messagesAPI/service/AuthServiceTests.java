package com.example.messagesAPI.service;

import com.example.messagesAPI.TestSocketIOConfig;
import com.example.messagesAPI.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    UserService userService;

    @Mock
    Authentication authentication;

    @Mock
    SecurityContextHolder securityContextHolder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Clear the SecurityContextHolder after each test
        SecurityContextHolder.clearContext();
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

    @Test
    void Test_GetAuthenticatedUser_Success(){
        //Create mock data
        User user = new User("test","test","test","test");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Simulate successful save
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        User result = authService.getAuthenticatedUser();

        //Check result
        verify(authentication, times(1)).isAuthenticated();
        verify(authentication, times(1)).getPrincipal();

        assertEquals(user,result);
    }

    @Test
    void Test_GetAuthenticatedUser_Error(){
        //Create mock data
        User user = new User("test","test","test","test");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Simulate successful save
        when(authentication.isAuthenticated()).thenReturn(false);
        User result = authService.getAuthenticatedUser();

        //Check result
        verify(authentication, times(1)).isAuthenticated();

        assertNull(result);
    }

}
