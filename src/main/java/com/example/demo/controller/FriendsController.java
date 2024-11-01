package com.example.demo.controller;

import com.example.demo.dto.friend.AcceptFriendRequest;
import com.example.demo.dto.friend.AddFriendRequest;
import com.example.demo.dto.friend.GetFriendsResponse;
import com.example.demo.service.FriendsService;
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
            return ResponseEntity.ok().build();
        }
        return  ResponseEntity.badRequest().build();
    }

    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriend(@RequestBody AcceptFriendRequest acceptFriendRequest)
    {
        if(friendsService.acceptFriendRequest(acceptFriendRequest.email())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends() {
        List<String> friends = friendsService.getFriends();

        if(friends != null){
            return ResponseEntity.ok(new GetFriendsResponse(friends));
        }
        return  ResponseEntity.badRequest().build();
    }
}
