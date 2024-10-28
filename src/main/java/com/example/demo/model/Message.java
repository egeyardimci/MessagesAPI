package com.example.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class Message {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private Date timestamp;
    private String content;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId sender;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId receiver;
    private boolean isGroupMessage;

    public Message(String content, ObjectId sender, ObjectId receiver,boolean isGroupMessage){
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.isGroupMessage = isGroupMessage;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
