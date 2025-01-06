package com.example.messagesAPI.controller;

import com.example.messagesAPI.dto.ErrorResponse;
import com.example.messagesAPI.dto.SuccessResponse;
import com.example.messagesAPI.dto.auth.LoginRequest;
import com.example.messagesAPI.dto.auth.LoginResponse;
import com.example.messagesAPI.dto.auth.RegisterRequest;
import com.example.messagesAPI.service.AuthService;
import com.example.messagesAPI.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JWTService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest)
    {
        if(authService.registerUser(
                registerRequest.name(),
                registerRequest.lastname(),
                registerRequest.email(),
                registerRequest.password())){
            return ResponseEntity.ok(new SuccessResponse("User created successfully!"));
        }
        else{
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to create user!"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        if (!authService.validateUser(loginRequest.email(), loginRequest.password())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid credentials!"));
        }
        else{
            String token = jwtService.createJWT(loginRequest.email());
            return ResponseEntity.ok(new LoginResponse(token));
        }
    }

}
