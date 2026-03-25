package com.example.houseagent.infrastructure.mock;

import com.example.houseagent.agents.layout.LayoutAgent;
import com.example.houseagent.domain.enums.RoomSizeCategory;
import com.example.houseagent.domain.enums.RoomType;
import com.example.houseagent.domain.enums.ZoneType;
import com.example.houseagent.domain.model.HouseLayout;
import com.example.houseagent.domain.model.LayoutRequest;
import com.example.houseagent.domain.model.RoomConnection;
import com.example.houseagent.domain.model.RoomPlan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MockLayoutAgent implements LayoutAgent {
    @Override
    public HouseLayout handle(LayoutRequest input) {
        List<RoomPlan> rooms = new ArrayList<>();
        for (int i = 1; i <= input.requirements().bedroomCount(); i++) {
            rooms.add(new RoomPlan("Bedroom " + i, RoomType.BEDROOM, RoomSizeCategory.MEDIUM, ZoneType.PRIVATE));
        }
        rooms.add(new RoomPlan("Living Room", RoomType.LIVING_ROOM, RoomSizeCategory.LARGE, ZoneType.SOCIAL));
        rooms.add(new RoomPlan("Kitchen", RoomType.KITCHEN,
                input.requirements().kitchenType().name().equals("OPEN_SPACE") ? RoomSizeCategory.LARGE : RoomSizeCategory.MEDIUM,
                ZoneType.SOCIAL));
        for (int i = 1; i <= input.requirements().toiletCount(); i++) {
            rooms.add(new RoomPlan("Bathroom " + i, RoomType.BATHROOM, RoomSizeCategory.SMALL, ZoneType.SERVICE));
        }

        List<RoomConnection> connections = List.of(
                new RoomConnection("Living Room", "Kitchen", "OPEN_PASSAGE"),
                new RoomConnection("Living Room", "Bedroom 1", "DOOR")
        );

        return new HouseLayout(rooms, connections);
    }
}
