package com.example.messagesAPI.dto.user;

import com.example.messagesAPI.model.Group;
import com.example.messagesAPI.model.Message;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class GroupWithLastMessage {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String name;
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private List<ObjectId> members;
    private Message lastMessage;

    public static GroupWithLastMessage fromGroup(Group group, Message lastMessage) {
        GroupWithLastMessage dto = new GroupWithLastMessage();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setMembers(group.getMembers());
        dto.setLastMessage(lastMessage);
        return dto;
    }
}