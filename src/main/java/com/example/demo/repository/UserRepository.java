package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserRepository {

    private final List<User> userList = new ArrayList<>();
    private int userCount = 0;


    public void registerUser(String name, String lastname, String email, String password){
        User newUser = new User(userCount,name,lastname,email,password);
        userCount++;
        userList.add(newUser);
    }

    public Boolean validateUser(String email, String password){
        for (User user : userList) {
            if (Objects.equals(user.getEmail(), email)) {
                if (Objects.equals(user.getPassword(), password)) {
                    return true;
                }
            }
        }
        return false;
    }
}
