package com.example.messagesAPI.dto.auth;

public record LoginRequest(
        String email,
        String password
) {}