package com.example.messagesAPI.repository;

import com.example.messagesAPI.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
    User findById(ObjectId id);
}
