package com.example.demo.service;

import com.example.demo.model.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FriendsService {
    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;

    public boolean addFriend(String email){
        User user = authService.getAuthenticatedUser();
        User targetUser = userService.findByEmail(email);

        if((user != null) && (targetUser != null)) {
            targetUser.getFriendRequests().add(user.getId());
            return userService.save(targetUser);
        }
        return false;
    }

    public List<String> getFriends(){
        User user = authService.getAuthenticatedUser();
        List<String> friendEmails = new ArrayList<>();

        if(user != null){
            for(ObjectId friendId : user.getFriends()){
                User friend = userService.findById(friendId);
                if(friend != null){
                    friendEmails.add(friend.getEmail());
                }
                else{
                    friendEmails.add("Null user!");
                }
            }
            return friendEmails;
        }
        return null;
    }

    public boolean acceptFriendRequest(String email){
        User user = authService.getAuthenticatedUser();
        User friendRequestUser = userService.findByEmail(email);

        if((user != null) && (friendRequestUser != null)){
            user.getFriends().add(friendRequestUser.getId());
            friendRequestUser.getFriends().add(user.getId());
            if(user.getFriendRequests().remove(friendRequestUser.getId())){
                return userService.save(user);
            }
        }

        return false;
    }

}
