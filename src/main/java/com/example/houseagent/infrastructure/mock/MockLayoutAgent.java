package com.example.houseagent.infrastructure.mock;

import com.example.houseagent.agents.layout.LayoutAgent;
import com.example.houseagent.domain.enums.RoomSizeCategory;
import com.example.houseagent.domain.enums.RoomType;
import com.example.houseagent.domain.enums.ZoneType;
import com.example.houseagent.domain.model.HouseLayout;
import com.example.houseagent.domain.model.LayoutRequest;
import com.example.houseagent.domain.model.RoomConnection;
import com.example.houseagent.domain.model.RoomPlan;

import java.util.ArrayList;
import java.util.List;

public class MockLayoutAgent implements LayoutAgent {
    @Override
    public HouseLayout handle(LayoutRequest input) {
        List<RoomPlan> rooms = new ArrayList<>();

        rooms.add(new RoomPlan("Entry", RoomType.ENTRY, RoomSizeCategory.SMALL, ZoneType.TRANSITION));
        rooms.add(new RoomPlan("Living Room", RoomType.LIVING_ROOM,
                input.requirements().livingAreaType().name().contains("OPEN") ? RoomSizeCategory.LARGE : RoomSizeCategory.MEDIUM,
                ZoneType.SOCIAL));
        rooms.add(new RoomPlan("Kitchen", RoomType.KITCHEN,
                input.requirements().kitchenType().name().contains("OPEN") ? RoomSizeCategory.MEDIUM : RoomSizeCategory.SMALL,
                ZoneType.SOCIAL));

        for (int i = 1; i <= input.requirements().bedroomCount(); i++) {
            rooms.add(new RoomPlan("Bedroom " + i, RoomType.BEDROOM, RoomSizeCategory.MEDIUM, ZoneType.PRIVATE));
        }

        for (int i = 1; i <= input.requirements().toiletCount(); i++) {
            rooms.add(new RoomPlan("Bathroom " + i, RoomType.BATHROOM, RoomSizeCategory.SMALL, ZoneType.SERVICE));
        }

        List<RoomConnection> connections = List.of(
                new RoomConnection("Entry", "Living Room", "direct"),
                new RoomConnection("Living Room", "Kitchen", "adjacent")
        );

        return new HouseLayout(rooms, connections);
    }
}
