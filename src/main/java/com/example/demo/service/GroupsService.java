package com.example.demo.service;

import com.example.demo.model.Group;
import com.example.demo.model.User;
import com.example.demo.repository.GroupsRepository;
import jakarta.websocket.OnClose;
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
                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public List<String> getMembersOfGroup(ObjectId groupId){
        try {
            Group group = groupsRepository.findById(groupId);
            List<String> memberEmails = new ArrayList<>();
            for(ObjectId memberId : group.getMembers()){
                User member = userService.findById(memberId);
                if(member != null) {
                    memberEmails.add(member.getEmail());
                }
            }
            return memberEmails;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
