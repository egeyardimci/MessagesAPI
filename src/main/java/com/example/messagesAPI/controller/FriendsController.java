package com.example.messagesAPI.controller;

import com.example.messagesAPI.dto.ErrorResponse;
import com.example.messagesAPI.dto.SuccessResponse;
import com.example.messagesAPI.dto.friend.AcceptFriendRequest;
import com.example.messagesAPI.dto.friend.AddFriendRequest;
import com.example.messagesAPI.dto.friend.GetFriendsResponse;
import com.example.messagesAPI.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FriendsController {

    @Autowired
    FriendsService friendsService;

    @PostMapping("/friends/add")
    public ResponseEntity<?> addFriend(@RequestBody AddFriendRequest addFriendRequest)
    {
        if(friendsService.addFriend(addFriendRequest.email())){
            return ResponseEntity.ok(new SuccessResponse("Friend request sent!"));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed to send friend request!"));
    }

    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriend(@RequestBody AcceptFriendRequest acceptFriendRequest)
    {
        if(friendsService.acceptFriendRequest(acceptFriendRequest.email())){
            return ResponseEntity.ok(new SuccessResponse("Friend request accepted!"));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed to accept friend request!"));
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends() {
        List<String> friends = friendsService.getFriends();

        if(friends != null){
            return ResponseEntity.ok(new GetFriendsResponse(friends));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed to get friends!"));
    }
}
