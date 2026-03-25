package com.example.houseagent.application.policy;

import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.RoutingDecision;
import com.example.houseagent.domain.enums.Severity;
import com.example.houseagent.domain.model.ConstraintIssue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IssueRoutingPolicyTest {

    private final IssueRoutingPolicy policy = new IssueRoutingPolicy();

    @Test
    void shouldRouteFixableIssuesToRegenerate() {
        var issues = List.of(new ConstraintIssue(IssueType.LAYOUT_FIXABLE, "x", List.of(), Severity.LOW));
        assertThat(policy.route(issues)).isEqualTo(RoutingDecision.REGENERATE);
    }

    @Test
    void shouldRouteUnsupportedToAbort() {
        var issues = List.of(new ConstraintIssue(IssueType.UNSUPPORTED_CASE, "x", List.of(), Severity.HIGH));
        assertThat(policy.route(issues)).isEqualTo(RoutingDecision.ABORT);
    }
}
