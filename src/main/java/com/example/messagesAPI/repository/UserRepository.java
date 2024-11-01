package com.example.messagesAPI.repository;

import com.example.messagesAPI.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
    User findById(ObjectId id);
}
