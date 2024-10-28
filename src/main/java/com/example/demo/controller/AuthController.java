package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.JWTService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JWTService jwtService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest)
    {
        authService.registerUser(
                registerRequest.name(),
                registerRequest.lastname(),
                registerRequest.email(),
                registerRequest.password());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        if (!authService.validateUser(loginRequest.email(), loginRequest.password())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtService.createJWT(loginRequest.email());
        return ResponseEntity.ok(new LoginResponse(token));
    }

}
