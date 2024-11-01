package com.example.demo.dto.friend;

import java.util.List;

public record GetFriendsResponse(
        List<String> friends
) {}
