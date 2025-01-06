package com.example.messagesAPI.dto.message;

public record SendMessageRequest(
        String content,
        String receiver,
        String receiverId,
        String sender,
        boolean groupMessage
) {

}
