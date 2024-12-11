package com.example.messagesAPI.service;

import com.example.messagesAPI.dto.user.GroupWithLastMessage;
import com.example.messagesAPI.dto.user.UserInfoGroupsResponse;
import com.example.messagesAPI.model.Group;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    AuthService authService;

    @Autowired
    GroupsService groupsService;

    public List<GroupWithLastMessage> getUserGroups(){
        User user = authService.getAuthenticatedUser();

        List<Group> groups = groupsService.filterGroupsByUser(user.getId());

        return groups.stream()
                .map(group -> {
                    Message lastMessage = groupsService.getLastMessageInGroup(group.getId());
                    return GroupWithLastMessage.fromGroup(group, lastMessage);
                })
                .toList();
    }

    public User getUserDetails(){
        return authService.getAuthenticatedUser();
    }
}
