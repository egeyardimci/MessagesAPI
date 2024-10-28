package com.example.demo.dto;

import com.example.demo.model.Message;

import java.util.List;

public record GetMessagesResponse(
        List<Message> messageList
) {
}
