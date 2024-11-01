package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Group {
    @Id
    private ObjectId id;
    private String name;
    private List<ObjectId> members;

    public Group(String name,List<ObjectId> members){
        this.members = members;
        this.name = name;
    }
}
