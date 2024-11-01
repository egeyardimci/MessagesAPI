package com.example.demo.dto.group;

import java.util.List;

public record GetGroupMembersResponse(
        List<String> members
) {
}
