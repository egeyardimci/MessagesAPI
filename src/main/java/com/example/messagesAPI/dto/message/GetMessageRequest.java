package com.example.messagesAPI.dto.message;

import com.example.messagesAPI.model.Message;
import org.bson.types.ObjectId;

import java.util.List;

public record GetMessageRequest(
        String participant
) {
}
