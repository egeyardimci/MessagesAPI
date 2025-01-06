package com.example.messagesAPI.dto.chat;

import org.bson.types.ObjectId;

import java.util.Objects;

public record ChatPairKey(ObjectId user1, ObjectId user2) {
    public ChatPairKey(ObjectId user1, ObjectId user2) {
        // Always store the smaller ID first to ensure consistent ordering
        if (user1.compareTo(user2) <= 0) {
            this.user1 = user1;
            this.user2 = user2;
        } else {
            this.user1 = user2;
            this.user2 = user1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatPairKey that)) return false;
        return (user1.equals(that.user1) && user2.equals(that.user2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1.toString(), user2.toString());
    }
}