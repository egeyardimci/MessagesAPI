package com.example.demo.dto.message;

import com.example.demo.model.Message;

import java.util.List;

public record GetMessagesResponse(
        List<Message> messageList
) {
}
