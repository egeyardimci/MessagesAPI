package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.JWTService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@RestController
public class FriendsController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/friends/add")
    public ResponseEntity<?> addFriend(@RequestBody AddFriendRequest addFriendRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user) {
                User targetUser = userRepository.findByEmail(addFriendRequest.email());
                targetUser.getFriendRequests().add(user.getId());
                userRepository.save(targetUser);
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriend(@RequestBody AcceptFriendRequest acceptFriendRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User friendRequestUser = userRepository.findByEmail(acceptFriendRequest.email());

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user) {
                for(ObjectId id : user.getFriendRequests()){
                    if(Objects.equals(id,friendRequestUser.getId())){
                        user.getFriends().add(friendRequestUser.getId());
                        user.getFriendRequests().remove(friendRequestUser.getId());
                        userRepository.save(user);
                        break;
                    }
                }
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends() {
        ArrayList<String> friends = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user) {
                System.out.println("here");
                for(ObjectId id : user.getFriends()){
                    User friend = userRepository.findById(id);
                    friends.add(friend.getEmail());
                }
            }
        }

        System.out.println(friends);
        return ResponseEntity.ok(new GetFriendsResponse(friends));
    }
}
