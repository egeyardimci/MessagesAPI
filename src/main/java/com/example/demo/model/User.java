package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private ArrayList<ObjectId> friends = new ArrayList<>();
    private ArrayList<ObjectId> friendRequests = new ArrayList<>();

    public User(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
