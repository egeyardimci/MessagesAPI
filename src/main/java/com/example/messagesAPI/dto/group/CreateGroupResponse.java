package com.example.messagesAPI.dto.group;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

public record CreateGroupResponse(
        @JsonSerialize(using = ToStringSerializer.class)
        ObjectId id
) {
}
