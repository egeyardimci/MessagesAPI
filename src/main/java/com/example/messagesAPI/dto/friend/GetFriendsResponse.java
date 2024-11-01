package com.example.messagesAPI.dto.friend;

import java.util.List;

public record GetFriendsResponse(
        List<String> friends
) {}
