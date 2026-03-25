package com.example.houseagent.infrastructure.mock;

import com.example.houseagent.agents.constraints.ConstraintsAgent;
import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.RoomType;
import com.example.houseagent.domain.enums.Severity;
import com.example.houseagent.domain.model.ConstraintCheckResult;
import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.model.HouseLayout;

import java.util.ArrayList;
import java.util.List;

public class MockConstraintsAgent implements ConstraintsAgent {
    @Override
    public ConstraintCheckResult handle(HouseLayout input) {
        List<ConstraintIssue> issues = new ArrayList<>();

        long bedroomCount = input.rooms().stream().filter(r -> r.roomType() == RoomType.BEDROOM).count();
        long bathroomCount = input.rooms().stream().filter(r -> r.roomType() == RoomType.BATHROOM).count();
        boolean hasLivingRoom = input.rooms().stream().anyMatch(r -> r.roomType() == RoomType.LIVING_ROOM);

        if (!hasLivingRoom) {
            issues.add(new ConstraintIssue(IssueType.LAYOUT_FIXABLE,
                    "Living room is missing", List.of(), Severity.HIGH));
        }

        if (bathroomCount == 0) {
            issues.add(new ConstraintIssue(IssueType.LAYOUT_FIXABLE,
                    "At least one bathroom is required", List.of(), Severity.CRITICAL));
        }

        if (bedroomCount > 5) {
            issues.add(new ConstraintIssue(IssueType.MISSING_INFORMATION,
                    "High bedroom count needs manual review for zoning preferences", List.of(), Severity.MEDIUM));
        }

        return new ConstraintCheckResult(issues.isEmpty(), issues);
    }
}
