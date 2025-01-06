package com.example.messagesAPI.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
public class Group {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String name;
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    private List<ObjectId> members;

    public Group(String name,List<ObjectId> members){
        this.members = members;
        this.name = name;
    }
}
