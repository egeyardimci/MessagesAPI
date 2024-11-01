package com.example.messagesAPI.dto.group;

import java.util.List;

public record GetGroupMembersResponse(
        List<String> members
) {
}
