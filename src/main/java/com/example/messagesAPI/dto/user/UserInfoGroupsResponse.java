package com.example.messagesAPI.dto.user;

import com.example.messagesAPI.model.Group;

import java.util.List;

public record UserInfoGroupsResponse(
        List<GroupWithLastMessage> groups
) {
}
