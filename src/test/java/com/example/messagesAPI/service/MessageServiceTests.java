package com.example.messagesAPI.service;

import com.example.messagesAPI.MessagesApplication;
import com.example.messagesAPI.model.Group;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.repository.MessagesRepository;
import com.example.messagesAPI.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MessageServiceTests {

    @Mock
    private MessagesRepository messagesRepository;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MessagesService messagesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void Test_GetMessages_Success(){
        //Create mock data
        User user = new User("test","test","test","test");
        Message message = new Message("test message", user.getId(),null,false);
        List<Message> messages = new ArrayList<>();
        messages.add(message);

        // Simulate successful save
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(messagesRepository.findBySenderOrReceiver(user.getId(),user.getId())).thenReturn(messages);
        List<Message> result = messagesService.getMessages();

        //Check result
        assertEquals(result,messages);
        verify(authService, times(1)).getAuthenticatedUser();
        verify(messagesRepository, times(1)).findBySenderOrReceiver(user.getId(),user.getId());
    }

    @Test
    void Test_GetGroupMessages_Success(){
        //Create mock data
        User user = new User("test","test","test","test");
        Group group = new Group();
        Message message = new Message("test message", group.getId(),null,true);
        List<Message> messages = new ArrayList<>();
        messages.add(message);

        // Simulate successful save
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(messagesRepository.findByReceiver(group.getId())).thenReturn(messages);
        List<Message> result = messagesService.getGroupMessages(group.getId());

        //Check result
        assertEquals(result,messages);
        verify(authService, times(1)).getAuthenticatedUser();
        verify(messagesRepository, times(1)).findByReceiver(group.getId());
    }

    @Test
    void Test_SendMessage_Success(){
        //Create mock data
        User user = new User("test","test","test","test");
        User receiver = new User("test2","test2","test2","test2");
        user.getFriends().add(receiver.getId());
        Message message = new Message("test message", user.getId(),null,false);

        // Simulate successful save
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(userService.findByEmail(receiver.getEmail())).thenReturn(receiver);
        when(messagesRepository.save(message)).thenReturn(message);
        boolean result = messagesService.sendMessageToFriend(message.getContent(),receiver.getEmail());

        //Check result
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

        verify(authService, times(1)).getAuthenticatedUser();
        verify(userService, times(1)).findByEmail(receiver.getEmail());
        verify(messagesRepository, times(1)).save(messageCaptor.capture());

        Message savedMessage = messageCaptor.getValue();

        assertTrue(result);
        assertEquals(message.getContent(),savedMessage.getContent());
        assertEquals(message.getSender(),savedMessage.getSender());
        assertEquals(message.getReceiver(),savedMessage.getReceiver());
        assertEquals(message.isGroupMessage(),savedMessage.isGroupMessage());
    }

    @Test
    void Test_SendGroupMessage_Success(){
        //Create mock data
        User user = new User("test","test","test","test");
        List<ObjectId> members = new ArrayList<>();
        members.add(user.getId());
        Group group = new Group("testGroup",members);
        Message message = new Message("test message", user.getId(),null,true);

        // Simulate successful save
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(messagesRepository.save(message)).thenReturn(message);
        boolean result = messagesService.sendMessageToGroup(message.getContent(),group.getId());

        //Check result
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

        verify(authService, times(1)).getAuthenticatedUser();
        verify(messagesRepository, times(1)).save(messageCaptor.capture());

        Message savedMessage = messageCaptor.getValue();

        assertTrue(result);
        assertEquals(message.getContent(),savedMessage.getContent());
        assertEquals(message.getSender(),savedMessage.getSender());
        assertEquals(message.getReceiver(),savedMessage.getReceiver());
        assertEquals(message.isGroupMessage(),savedMessage.isGroupMessage());
    }

}
