package com.example.messagesAPI.repository;

import com.example.messagesAPI.model.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MessagesRepository extends MongoRepository<Message, ObjectId> {
    List<Message> findBySenderOrReceiver(ObjectId sender, ObjectId receiver);
    List<Message> findByReceiver(ObjectId receiver);

    Message findFirstByReceiverOrderByTimestampDesc(ObjectId receiver);

    List<Message> findBySenderAndIsGroupMessageFalseOrReceiverAndIsGroupMessageFalse(ObjectId sender, ObjectId receiver);

    @Query(value = "{ 'isGroupMessage': false, $or: [ " +
            "{ 'sender': ?0, 'receiver': ?1 }, " +
            "{ 'sender': ?1, 'receiver': ?0 } " +
            "] }",
            sort = "{ 'timestamp': -1 }")
    List<Message> findMessagesBetweenUsers(ObjectId user1, ObjectId user2);

    default Optional<Message> findLastMessageBetweenUsers(ObjectId user1, ObjectId user2) {
        List<Message> messages = findMessagesBetweenUsers(user1, user2);
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages.getFirst());
    }
}
