package com.example.demo.dto;

import org.springframework.web.bind.annotation.RequestBody;

public record RegisterRequest(
        String name,
        String lastname,
        String email,
        String password
) {}