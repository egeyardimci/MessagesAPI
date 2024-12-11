package com.example.messagesAPI.repository;

import com.example.messagesAPI.model.Group;
import com.example.messagesAPI.model.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupsRepository extends MongoRepository<Group,String> {
    Group findById(ObjectId id);
    @Query("{ 'members': ?0 }")
    List<Group> findByMemberId(ObjectId memberId);
}
