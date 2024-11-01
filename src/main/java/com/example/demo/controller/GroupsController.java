package com.example.demo.controller;

import com.example.demo.dto.group.*;
import com.example.demo.model.Message;
import com.example.demo.service.GroupsService;
import com.example.demo.service.MessagesService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroupsController {

    @Autowired
    GroupsService groupsService;
    @Autowired
    MessagesService messagesService;

    @PostMapping("/groups/create")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest createGroupRequest)
    {
        ObjectId groupId = groupsService.createGroup(createGroupRequest.name(),createGroupRequest.members());

        if(groupId !=null){
            return ResponseEntity.ok(new CreateGroupResponse(groupId));
        }
        return  ResponseEntity.badRequest().build();
    }

    @PostMapping("/groups/{groupId}/add-member")
    public ResponseEntity<?> addMember(@PathVariable("groupId") ObjectId groupId, @RequestBody AddMemberRequest addMemberRequest)
    {
        if(groupsService.addMemberToGroup(groupId,addMemberRequest.email())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/groups/{groupId}/send")
    public ResponseEntity<?> sendMessage(@PathVariable("groupId") ObjectId groupId, @RequestBody SendGroupMessageRequest sendGroupMessageRequest)
    {
        if(messagesService.sendMessageToGroup(sendGroupMessageRequest.content(),groupId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/groups/{groupId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable("groupId") ObjectId groupId)
    {
        List<Message> groupMessages = messagesService.getGroupMessages(groupId);

        if(groupMessages != null){
            return ResponseEntity.ok(new GetGroupMessagesResponse(groupMessages));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<?> getMembers(@PathVariable("groupId") ObjectId groupId)
    {
        List<String> groupMembers = groupsService.getMembersOfGroup(groupId);

        if(groupMembers != null){
            return ResponseEntity.ok(new GetGroupMembersResponse(groupMembers));
        }
        return ResponseEntity.badRequest().build();
    }

}
