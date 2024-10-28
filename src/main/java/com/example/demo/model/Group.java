package com.example.demo.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public class Group {
    @Id
    private ObjectId id;
    private ArrayList<ObjectId> members;
}
