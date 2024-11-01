package com.example.demo.dto.group;

import java.util.ArrayList;

public record CreateGroupRequest(
        String name,
        ArrayList<String> members
) {
}
