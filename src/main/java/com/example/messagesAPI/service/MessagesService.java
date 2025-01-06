package com.example.messagesAPI.service;

import com.example.messagesAPI.dto.chat.ChatPairKey;
import com.example.messagesAPI.dto.chat.UniqueChats;
import com.example.messagesAPI.dto.user.UserInfoResponse;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.repository.MessagesRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessagesService {

    @Autowired
    MessagesRepository messagesRepository;
    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;

    public List<Message> getMessages(){

        User user = authService.getAuthenticatedUser();

        if(user != null) {
            try {
                return messagesRepository.findBySenderOrReceiver(user.getId(), user.getId());
            }
            catch (Exception e){
                return null;
            }
        }
        return null;
    }

    public List<Message> getGroupMessages(ObjectId groupId){

        User user = authService.getAuthenticatedUser();

        if(user != null) {
            try {
                return messagesRepository.findByReceiver(groupId);
            }
            catch (Exception e){
                return null;
            }
        }
        return null;
    }

    public Message sendMessageToFriend(String message, String receiver, String sender){
        User user = userService.findByEmail(sender);
        User receiverUser = userService.findByEmail(receiver);

        if ((user != null) && (receiverUser != null)) {
            ObjectId receiverId = receiverUser.getId();
            if (user.getFriends().contains(receiverId)) {
                try {
                    return messagesRepository.save(new Message(
                            message,
                            user.getId(),
                            receiverId,
                            false,""));
                }
                catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
            //Not friends
            return null;
        }
        //User was null
        return null;
    }

    public Message sendMessageToGroup(String message, ObjectId groupId,String sender){
        User user = userService.findByEmail(sender);

        if ((user != null)) {
            try {
                return messagesRepository.save(new Message(
                        message,
                        user.getId(),
                        groupId,
                        true,
                        user.getName()));
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        //User was null
        return null;
    }

    public List<UniqueChats> getUniqueChatsWithDetails() {
        User user = authService.getAuthenticatedUser();

        List<Message> nonGroupMessages = messagesRepository.findBySenderAndIsGroupMessageFalseOrReceiverAndIsGroupMessageFalse(user.getId(),user.getId());

        // Create a map to store the last message for each chat pair
        Set<ChatPairKey> chatPairs = new HashSet<>();

        // Process messages to find the last message for each unique chat pair
        for (Message message : nonGroupMessages) {
            ChatPairKey chatKey = new ChatPairKey(message.getSender(), message.getReceiver());

            chatPairs.add(chatKey);
        }


        // Convert to response DTOs with user details
        return chatPairs.stream()
                .map(entry -> {
                    Optional<Message> lastMessage = messagesRepository.findLastMessageBetweenUsers(entry.user1(),entry.user2());
                    User user1 = userService.findById(entry.user1());
                    User user2 = userService.findById(entry.user2());

                    UserInfoResponse participant = null;
                    if(Objects.equals(user.getEmail(), user1.getEmail())){
                        participant = new UserInfoResponse(user2.getName(),user2.getLastName(),user2.getEmail(),user2.getId());
                    }
                    else{
                        participant = new UserInfoResponse(user1.getName(),user1.getLastName(),user1.getEmail(),user1.getId());
                    }

                    return new UniqueChats(
                            participant,
                            lastMessage
                    );
                })
                .collect(Collectors.toList());
    }

    public List<Message> getMessagesFromChat(ObjectId participant){
        User user = authService.getAuthenticatedUser();
        return messagesRepository.findMessagesBetweenUsers(user.getId(),participant).reversed();
    }
}
