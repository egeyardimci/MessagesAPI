package com.example.messagesAPI.dto.message;

import com.example.messagesAPI.model.Message;

import java.util.List;

public record GetMessagesResponse(
        List<Message> messageList
) {
}
