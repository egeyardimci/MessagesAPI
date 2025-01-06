package com.example.messagesAPI.service;

import com.example.messagesAPI.dto.user.UserInfoResponse;
import com.example.messagesAPI.model.Group;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.repository.GroupsRepository;
import com.example.messagesAPI.repository.MessagesRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupsService {

    @Autowired
    GroupsRepository groupsRepository;
    @Autowired
    MessagesRepository messagesRepository;
    @Autowired
    UserService userService;

    public ObjectId createGroup(String name, ArrayList<String> members) {
        List<ObjectId> memberIds = new ArrayList<>();

        for(String email: members){
            User member = userService.findByEmail(email);
            if(member != null){
                memberIds.add(member.getId());
            }
        }

        try {
            Group createdGroup = groupsRepository.save(new Group(name,memberIds));
            return createdGroup.getId();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean addMemberToGroup(ObjectId groupId, String email){
        try {
            Group group = groupsRepository.findById(groupId);
            User userToAdd = userService.findByEmail(email);

            if(userToAdd != null){
                group.getMembers().add(userToAdd.getId());
                groupsRepository.save(group);
                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public List<UserInfoResponse> getMembersOfGroup(ObjectId groupId){
        try {
            Group group = groupsRepository.findById(groupId);
            List<UserInfoResponse> members = new ArrayList<>();
            for(ObjectId memberId : group.getMembers()){
                User member = userService.findById(memberId);
                if(member != null) {
                    members.add(new UserInfoResponse(member.getName(), member.getLastName(), member.getEmail(), member.getId()));
                }
            }
            return members;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Group> filterGroupsByUser(ObjectId uid){
        try {
            return groupsRepository.findByMemberId(uid);
        }catch (Exception e){
            return null;
        }
    }

    public Message getLastMessageInGroup(ObjectId gid){
        try {
            return messagesRepository.findFirstByReceiverOrderByTimestampDesc(gid);
        }catch (Exception e){
            return null;
        }
    }
}
