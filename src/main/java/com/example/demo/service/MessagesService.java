package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.repository.MessagesRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean sendMessageToFriend(String message, String receiver){
        User user = authService.getAuthenticatedUser();
        User receiverUser = userService.findByEmail(receiver);

        System.out.println(receiverUser.getEmail());

        if ((user != null) && (receiverUser != null)) {
            ObjectId receiverId = receiverUser.getId();
            if (user.getFriends().contains(receiverId)) {
                System.out.println("inbaba");
                try {
                    messagesRepository.save(new Message(
                            message,
                            user.getId(),
                            receiverId,
                            false));
                    return true;
                }
                catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }
            //Not friends
            return false;
        }
        //User was null
        return false;
    }

    public boolean sendMessageToGroup(String message, ObjectId groupId){
        User user = authService.getAuthenticatedUser();

        if ((user != null)) {
            try {
                messagesRepository.save(new Message(
                        message,
                        user.getId(),
                        groupId,
                        true));
                return true;
            }
            catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
        //User was null
        return false;
    }
}
