package com.example.messagesAPI.service;

import com.example.messagesAPI.model.User;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserService userService;

    public boolean registerUser(String name, String lastname, String email, String password){
        return userService.save(new User(name,lastname,email,password));
    }

    public boolean validateUser(String email, String password){
        User user = userService.findByEmail(email);

        if(user != null) {
            return Objects.equals(user.getPassword(), password);
        }
        else return false;
    }

    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user){
                return user;
            }
        }

        return null;
    }

}
