package com.example.houseagent.infrastructure.mock;

import com.example.houseagent.agents.requirements.RequirementsAgent;
import com.example.houseagent.domain.model.HouseRequirements;
import com.example.houseagent.domain.model.UserRequest;

public class MockRequirementsAgent implements RequirementsAgent {
    @Override
    public HouseRequirements handle(UserRequest input) {
        return new HouseRequirements(
                input.totalArea(),
                input.bedroomCount(),
                input.residentsCount(),
                input.toiletCount(),
                input.kitchenType(),
                input.livingAreaType()
        );
    }
}
