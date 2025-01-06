package com.example.messagesAPI.controller;

import com.example.messagesAPI.dto.ErrorResponse;
import com.example.messagesAPI.dto.SuccessResponse;
import com.example.messagesAPI.dto.group.*;
import com.example.messagesAPI.dto.user.UserInfoResponse;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.service.GroupsService;
import com.example.messagesAPI.service.MessagesService;
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
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed to create group!"));
    }

    @PostMapping("/groups/{groupId}/add-member")
    public ResponseEntity<?> addMember(@PathVariable("groupId") ObjectId groupId, @RequestBody AddMemberRequest addMemberRequest)
    {
        if(groupsService.addMemberToGroup(groupId,addMemberRequest.email())){
            return ResponseEntity.ok(new SuccessResponse("User added to group!"));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed to add user to group!"));
    }

    @GetMapping("/groups/{groupId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable("groupId") ObjectId groupId)
    {
        List<Message> groupMessages = messagesService.getGroupMessages(groupId);

        if(groupMessages != null){
            return ResponseEntity.ok(new GetGroupMessagesResponse(groupMessages));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed retrieve messages from the group!"));
    }

    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<?> getMembers(@PathVariable("groupId") ObjectId groupId)
    {
        List<UserInfoResponse> groupMembers = groupsService.getMembersOfGroup(groupId);

        if(groupMembers != null){
            return ResponseEntity.ok(new GetGroupMembersResponse(groupMembers));
        }
        return ResponseEntity.badRequest().body(new ErrorResponse("Failed retrieve members of group!"));
    }

}
