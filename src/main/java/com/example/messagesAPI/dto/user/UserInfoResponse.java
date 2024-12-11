package com.example.messagesAPI.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

public record UserInfoResponse(
        String name,
        String lastname,
        String email,
        @JsonSerialize(using = ToStringSerializer.class)
        ObjectId id
) {
}
