package com.example.messagesAPI.service;

import com.example.messagesAPI.dto.user.UserInfoResponse;
import com.example.messagesAPI.model.Group;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    public boolean save(User user){
        try {
            userRepository.save(user);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public User findByEmail(String email){
        try {
            return userRepository.findByEmail(email);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public User findById(ObjectId userId){
        try {
            return userRepository.findById(userId);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
