package com.example.messagesAPI.dto.auth;

public record RegisterRequest(
        String name,
        String lastname,
        String email,
        String password
) {}