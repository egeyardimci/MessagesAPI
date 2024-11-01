package com.example.demo.controller;

import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.LoginResponse;
import com.example.demo.dto.auth.RegisterRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.JWTService;
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
        if(authService.registerUser(
                registerRequest.name(),
                registerRequest.lastname(),
                registerRequest.email(),
                registerRequest.password())){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        if (!authService.validateUser(loginRequest.email(), loginRequest.password())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        }
        else{
        String token = jwtService.createJWT(loginRequest.email());
        return ResponseEntity.ok(new LoginResponse(token));
        }
    }

}
