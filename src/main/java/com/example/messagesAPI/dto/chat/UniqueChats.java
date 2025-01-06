package com.example.messagesAPI.dto.chat;

import com.example.messagesAPI.dto.user.UserInfoResponse;
import com.example.messagesAPI.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniqueChats {
    private UserInfoResponse participant;
    private Optional<Message> lastMessage;
}