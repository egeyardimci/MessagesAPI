package com.example.demo.dto;

import org.bson.types.ObjectId;

public record SendMessageRequest(
        String content,
        String receiver
) {

}
