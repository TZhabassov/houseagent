package com.example.houseagent.domain.model;

import com.example.houseagent.domain.enums.PlanningStatus;

import java.util.List;

public record PlanningResult(
        HouseRequirements requirements,
        HouseLayout layout,
        PlanningStatus status,
        List<ConstraintIssue> issues,
        String reason) {
}
