package com.example.demo.controller;

import com.example.demo.dto.GetMessagesResponse;
import com.example.demo.dto.SendMessageRequest;
import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.repository.MessagesRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessagesController {

    @Autowired
    MessagesRepository messagesRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                List<Message> messages = messagesRepository.findBySenderOrReceiver(((User) principal).getId(),((User) principal).getId());
                System.out.println(messages);
                return ResponseEntity.ok(new GetMessagesResponse(messages));
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/messages/send")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest sendMessageRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                //If they are friends
                if(((User) principal).getFriends().contains(
                        userRepository.findByEmail(sendMessageRequest.receiver()).getId())) {
                    messagesRepository.save(new Message(
                            sendMessageRequest.content(),
                            ((User) principal).getId(),
                            userRepository.findByEmail(sendMessageRequest.receiver()).getId(),
                            false
                    ));
                    return ResponseEntity.ok().build();
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
