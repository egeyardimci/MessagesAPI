package com.example.messagesAPI.model;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
public class ChatClient {
    private ObjectId userId;
    private SocketIOClient socket;
}
