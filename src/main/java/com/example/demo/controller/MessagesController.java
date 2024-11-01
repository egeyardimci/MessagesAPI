package com.example.demo.controller;

import com.example.demo.dto.message.GetMessagesResponse;
import com.example.demo.dto.message.SendMessageRequest;
import com.example.demo.model.Message;
import com.example.demo.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessagesController {

    @Autowired
    MessagesService messagesService;

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages()
    {
        List<Message> messages = messagesService.getMessages();

        if(messages != null){
            return ResponseEntity.ok(new GetMessagesResponse(messages));
        }
        return  ResponseEntity.badRequest().build();
    }

    @PostMapping("/messages/send")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest sendMessageRequest) {

        if(messagesService.sendMessageToFriend(sendMessageRequest.content(),sendMessageRequest.receiver())){
            return ResponseEntity.ok().build();
        }
        else{
            return  ResponseEntity.badRequest().build();
        }
    }
}
