package com.example.demo.dto.message;

public record SendMessageRequest(
        String content,
        String receiver
) {

}
