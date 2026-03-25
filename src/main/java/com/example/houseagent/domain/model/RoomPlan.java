package com.example.houseagent.domain.model;

import com.example.houseagent.domain.enums.RoomSizeCategory;
import com.example.houseagent.domain.enums.RoomType;
import com.example.houseagent.domain.enums.ZoneType;

public record RoomPlan(
        String roomName,
        RoomType roomType,
        RoomSizeCategory sizeCategory,
        ZoneType zoneType) {
}
