package com.example.demo.dto.group;

import com.example.demo.model.Message;

import java.util.List;

public record GetGroupMessagesResponse(
        List<Message> messages
) {
}
