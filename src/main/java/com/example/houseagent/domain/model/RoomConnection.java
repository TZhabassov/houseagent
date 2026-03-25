package com.example.houseagent.domain.model;

public record RoomConnection(
        String fromRoom,
        String toRoom,
        String connectionType) {
}
