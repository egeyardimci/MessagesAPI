package com.example.messagesAPI.service;

import com.example.messagesAPI.dto.user.UserInfoResponse;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<UserInfoResponse> getFriends(){
        User user = authService.getAuthenticatedUser();
        List<UserInfoResponse> friends = new ArrayList<>();

        if(user != null){
            for(ObjectId friendId : user.getFriends()){
                User friend = userService.findById(friendId);
                UserInfoResponse friendInfo = new UserInfoResponse(friend.getName(),friend.getLastName(),friend.getEmail(),friend.getId());
                friends.add(friendInfo);
            }
            return friends;
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
                return userService.save(user) && userService.save(friendRequestUser);
            }
        }

        return false;
    }

    public List<UserInfoResponse> getFriendRequests(){
        User user = authService.getAuthenticatedUser();
        List<UserInfoResponse> friendRequestUsers = new ArrayList<>();


        for(ObjectId id : user.getFriendRequests()){
            User tempUser = userService.findById(id);
            UserInfoResponse tempUIR = new UserInfoResponse(tempUser.getName(),tempUser.getLastName(),tempUser.getEmail(),tempUser.getId());
            friendRequestUsers.add(tempUIR);
        }

        return friendRequestUsers;
    }

}
