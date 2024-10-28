package com.example.demo.service;

import com.example.demo.model.User;

import java.security.Key;
import java.util.*;

import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    public void registerUser(String name, String lastname, String email, String password){
        userRepository.save(new User(name,lastname,email,password));
    }

    public Boolean validateUser(String email, String password){
        User user = userRepository.findByEmail(email);
        if(user != null) {
            System.out.println(user.getId());
            return Objects.equals(user.getPassword(), password);
        }
        else return false;
    }

}
