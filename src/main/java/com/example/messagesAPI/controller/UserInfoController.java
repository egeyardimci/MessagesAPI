package com.example.messagesAPI.controller;

import com.example.messagesAPI.dto.ErrorResponse;
import com.example.messagesAPI.dto.chat.UniqueChats;
import com.example.messagesAPI.dto.chat.UniqueChatsResponse;
import com.example.messagesAPI.dto.user.GroupWithLastMessage;
import com.example.messagesAPI.dto.user.UserInfoGroupsResponse;
import com.example.messagesAPI.dto.user.UserInfoResponse;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.service.MessagesService;
import com.example.messagesAPI.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MessagesService messagesService;


    @GetMapping("/user/details")
    public ResponseEntity<?> user() {
        User user = userInfoService.getUserDetails();
        if(user != null){
            return ResponseEntity.ok(new UserInfoResponse(user.getName(),user.getLastName(),user.getEmail(),user.getId()));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed to get user info!"));
    }

    @GetMapping("/user/groups")
    public ResponseEntity<?> groups() {
        List<GroupWithLastMessage> groups = userInfoService.getUserGroups();
        if(groups != null){
            return ResponseEntity.ok(new UserInfoGroupsResponse(groups));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed to get user info!"));
    }

    @GetMapping("/user/chats")
    public ResponseEntity<?> getMessages()
    {
        List<UniqueChats> chats = messagesService.getUniqueChatsWithDetails();

        if(chats != null){
            return ResponseEntity.ok(new UniqueChatsResponse(chats));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed retrieve messages!"));
    }

}
