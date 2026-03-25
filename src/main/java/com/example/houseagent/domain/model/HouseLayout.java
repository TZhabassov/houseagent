package com.example.houseagent.domain.model;

import java.util.List;

public record HouseLayout(
        List<RoomPlan> rooms,
        List<RoomConnection> connections
) {
}
