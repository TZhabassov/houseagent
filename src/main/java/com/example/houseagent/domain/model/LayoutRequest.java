package com.example.houseagent.domain.model;

import java.util.List;

public record LayoutRequest(
        HouseRequirements requirements,
        HouseLayout previousLayout,
        List<ConstraintIssue> issues
) {
}
