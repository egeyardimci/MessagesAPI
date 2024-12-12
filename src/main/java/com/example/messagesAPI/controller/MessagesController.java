package com.example.messagesAPI.controller;

import com.example.messagesAPI.dto.ErrorResponse;
import com.example.messagesAPI.dto.SuccessResponse;
import com.example.messagesAPI.dto.message.GetMessageRequest;
import com.example.messagesAPI.dto.message.GetMessagesResponse;
import com.example.messagesAPI.dto.message.SendMessageRequest;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.service.MessagesService;
import org.bson.types.ObjectId;
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
    public ResponseEntity<?> messages()
    {
        List<Message> messages = messagesService.getMessages();

        if(messages != null){
            return ResponseEntity.ok(new GetMessagesResponse(messages));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed retrieve messages!"));
    }

    @PostMapping("/messages/get")
    public ResponseEntity<?> getMessages(@RequestBody GetMessageRequest getMessageRequest)
    {
        ObjectId participantId = new ObjectId(getMessageRequest.participant());
        List<Message> messages = messagesService.getMessagesFromChat(participantId);

        if(messages != null){
            return ResponseEntity.ok(new GetMessagesResponse(messages));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed retrieve messages!"));
    }

    @PostMapping("/messages/send")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest sendMessageRequest) {

        if(messagesService.sendMessageToFriend(sendMessageRequest.content(),sendMessageRequest.receiver())){
            return ResponseEntity.ok(new SuccessResponse("Message successfully sent!"));
        }
        else{
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to send message!"));
        }
    }
}
