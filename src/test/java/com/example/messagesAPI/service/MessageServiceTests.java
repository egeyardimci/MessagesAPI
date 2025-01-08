package com.example.messagesAPI.service;

import com.example.messagesAPI.TestSocketIOConfig;
import com.example.messagesAPI.model.Group;
import com.example.messagesAPI.model.Message;
import com.example.messagesAPI.model.User;
import com.example.messagesAPI.repository.MessagesRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        Message message = new Message("test message", user.getId(),null,false, user.getName());
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
        Message message = new Message("test message", group.getId(),null,true,user.getName());
        List<Message> messages = new ArrayList<>();
        messages.add(message);

        // Simulate successful save
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(messagesRepository.findByReceiver(group.getId())).thenReturn(messages);
        List<Message> result = messagesService.getGroupMessages(group.getId());

        //Check result
        assertEquals(result,messages);
        verify(messagesRepository, times(1)).findByReceiver(group.getId());
    }

    @Test
    void Test_SendMessage_Success(){
        //Create mock data
        User user = new User("test","test","test","test");
        User receiver = new User("test2","test2","test2","test2");
        user.getFriends().add(receiver.getId());
        Message message = new Message("test message", user.getId(),null,false,receiver.getName());
        user.getFriends().add(receiver.getId());


        // Simulate successful save
        when(userService.findByEmail(receiver.getEmail())).thenReturn(receiver);
        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        when(messagesRepository.save(any(Message.class))).thenReturn(message);
        Message savedMessage = messagesService.sendMessageToFriend(message.getContent(),receiver.getEmail(),user.getEmail());


        //Check result
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

        verify(userService, times(1)).findByEmail(receiver.getEmail());
        verify(userService, times(1)).findByEmail(user.getEmail());
        verify(messagesRepository, times(1)).save(messageCaptor.capture());

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
        Message message = new Message("test message", user.getId(),null,true,user.getName());

        // Simulate successful save
        when(messagesRepository.save(any(Message.class))).thenReturn(message);
        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        Message savedMessage = messagesService.sendMessageToGroup(message.getContent(),group.getId(),user.getEmail());

        //Check result
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

        verify(userService, times(1)).findByEmail(user.getEmail());
        verify(messagesRepository, times(1)).save(messageCaptor.capture());


        assertEquals(message.getContent(),savedMessage.getContent());
        assertEquals(message.getSender(),savedMessage.getSender());
        assertEquals(message.getReceiver(),savedMessage.getReceiver());
        assertEquals(message.isGroupMessage(),savedMessage.isGroupMessage());
    }

}
