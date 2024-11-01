package com.example.messagesAPI.repository;

import com.example.messagesAPI.model.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessagesRepository extends MongoRepository<Message, ObjectId> {
    List<Message> findBySenderOrReceiver(ObjectId sender, ObjectId receiver);
    List<Message> findByReceiver(ObjectId receiver);
}
