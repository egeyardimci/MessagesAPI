package com.example.messagesAPI.dto.chat;

import java.util.List;

public record UniqueChatsResponse(
        List<UniqueChats> uniqueChats
) {
}
