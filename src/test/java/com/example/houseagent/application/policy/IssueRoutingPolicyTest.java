package com.example.houseagent.application.policy;

import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.RoutingDecision;
import com.example.houseagent.domain.enums.Severity;
import com.example.houseagent.domain.model.ConstraintIssue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IssueRoutingPolicyTest {

    private final IssueRoutingPolicy policy = new IssueRoutingPolicy();

    @Test
    void shouldRegenerateForFixableIssue() {
        RoutingDecision decision = policy.decide(List.of(new ConstraintIssue(
                IssueType.LAYOUT_FIXABLE, "Fix", List.of(), Severity.MEDIUM
        )));

        assertEquals(RoutingDecision.REGENERATE, decision);
    }

    @Test
    void shouldRequestClarificationForMissingInformation() {
        RoutingDecision decision = policy.decide(List.of(new ConstraintIssue(
                IssueType.MISSING_INFORMATION, "Need details", List.of(), Severity.HIGH
        )));

        assertEquals(RoutingDecision.REQUEST_CLARIFICATION, decision);
    }
}
