package com.example.houseagent.infrastructure.mock;

import com.example.houseagent.agents.constraints.ConstraintsAgent;
import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.Severity;
import com.example.houseagent.domain.model.ConstraintCheckResult;
import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.model.HouseLayout;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MockConstraintsAgent implements ConstraintsAgent {
    @Override
    public ConstraintCheckResult handle(HouseLayout input) {
        List<ConstraintIssue> issues = new ArrayList<>();
        if (input.rooms() == null || input.rooms().isEmpty()) {
            issues.add(new ConstraintIssue(IssueType.UNSUPPORTED_CASE, "Layout has no rooms", List.of(), Severity.HIGH));
        }
        boolean hasLiving = input.rooms().stream().anyMatch(room -> room.roomName().equalsIgnoreCase("Living Room"));
        if (!hasLiving) {
            issues.add(new ConstraintIssue(IssueType.LAYOUT_FIXABLE, "Living room missing", List.of("Living Room"), Severity.MEDIUM));
        }
        return new ConstraintCheckResult(issues.isEmpty(), issues);
    }
}
