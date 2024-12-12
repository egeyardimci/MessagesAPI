package com.example.messagesAPI.dto.group;

import com.example.messagesAPI.model.Message;

import java.util.List;

public record GetGroupMessagesResponse(
        List<Message> messageList
) {
}
