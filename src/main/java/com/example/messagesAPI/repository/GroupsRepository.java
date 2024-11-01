package com.example.messagesAPI.repository;

import com.example.messagesAPI.model.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface GroupsRepository extends MongoRepository<Group,String> {
    Group findById(ObjectId id);
}
