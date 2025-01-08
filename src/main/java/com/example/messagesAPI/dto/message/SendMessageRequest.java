package com.example.messagesAPI.dto.message;

public record SendMessageRequest(
        String content,
        String receiver,
        String receiverId,
        boolean groupMessage
) {

}
