package com.example.messagesAPI.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.messagesAPI.dto.chat.ChatPairKey;
import com.example.messagesAPI.dto.message.SendMessageRequest;
import com.example.messagesAPI.model.ChatClient;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SocketIOService {

    @Autowired
    UserService userService;

    @Autowired
    MessagesService messagesService;

    User user = null;

    private final SocketIOServer server;
    // Store connected clients with their user IDs
    private final Map<Integer, List<SocketIOClient>> connectedClients = new HashMap<>();

    public SocketIOService(SocketIOServer server) {
        this.server = server;
    }

    @PostConstruct
    public void startServer() {
        server.start();

        // Handle connection with authentication
        server.addConnectListener(client -> {
            // Get user ID from handshake auth data
            user = userService.findByEmail(client.getHandshakeData().getHttpHeaders().get("email"));
            System.out.println(user);
            System.out.println(client.getHandshakeData().getHttpHeaders().get("email"));
            System.out.println(client.getHandshakeData().getHttpHeaders().get("receiverId"));
            System.out.println(client.getHandshakeData().getHttpHeaders().get("isGroup"));

            String receiverId = client.getHandshakeData().getSingleUrlParam("receiverId");
            boolean isGroup = Boolean.parseBoolean(client.getHandshakeData().getSingleUrlParam("isGroup"));

            if (receiverId != null && user != null) {

                int hashValue = 0;

                if(!isGroup) {
                    ChatPairKey chatPairKey = new ChatPairKey(new ObjectId(receiverId), user.getId());
                    hashValue = chatPairKey.hashCode();
                }
                else{
                    hashValue = receiverId.hashCode();
                }
                // Store client information
                List<SocketIOClient> clients = connectedClients.get(hashValue);
                if(clients == null){
                    clients = new ArrayList<>();
                }

                connectedClients.forEach((key, value) -> {
                    value.removeIf(chatClient -> chatClient.getSessionId().equals(client.getSessionId()));
                });

                clients.add(client);

                connectedClients.put(hashValue, clients);

                log.info("Client connected - User ID: {}, Session ID: {}",
                        receiverId, client.getSessionId());

                System.out.println("Connected clients: " + connectedClients);
            }

            else {
                client.disconnect();
            }
        });

        server.addDisconnectListener(client -> {
            //find user id of the client and delete it from connectedClients
            connectedClients.forEach((key, value) -> {
                value.removeIf(chatClient -> chatClient.getSessionId().equals(client.getSessionId()));
            });

            System.out.println("Connected clients: " + connectedClients);
        });


        // Handle private messages
        server.addEventListener("message", SendMessageRequest.class,
                (client, message, ackRequest) -> {
                    //save message to db
                    user = userService.findByEmail(client.getHandshakeData().getHttpHeaders().get("email"));
                    Message savedMessage = null;
                    if(message.groupMessage()){
                        savedMessage = messagesService.sendMessageToGroup(message.content(), new ObjectId(message.receiverId()), user.getEmail());
                    } else {
                        savedMessage  = messagesService.sendMessageToFriend(message.content(), message.receiver(),user.getEmail());
                    }
                    //forward message to socket
                    int hashValue = 0;
                    System.out.println("IS GROUP MESSAGE: " + message.groupMessage());
                    if(!message.groupMessage()) {
                        System.out.println("insidie it");
                        ChatPairKey chatPairKey = new ChatPairKey(new ObjectId(message.receiverId()), user.getId());
                        hashValue = chatPairKey.hashCode();
                    }
                    else{
                        hashValue = message.receiverId().hashCode();
                    }
                    System.out.println("HASH VALUE: " + hashValue);
                    Message finalSavedMessage = savedMessage;
                    connectedClients.get(hashValue).forEach(chatClient -> {
                        chatClient.sendEvent("message", finalSavedMessage);
                    });

                });

    }

    @PreDestroy
    public void stopServer() {
        server.stop();
    }
}
