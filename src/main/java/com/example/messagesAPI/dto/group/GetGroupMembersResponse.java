package com.example.messagesAPI.dto.group;

import com.example.messagesAPI.dto.user.UserInfoResponse;

import java.util.List;

public record GetGroupMembersResponse(
        List<UserInfoResponse> members
) {
}
