package com.example.houseagent.application.policy;

import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.RoutingDecision;
import com.example.houseagent.domain.model.ConstraintIssue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IssueRoutingPolicy {
    public RoutingDecision route(List<ConstraintIssue> issues) {
        if (issues == null || issues.isEmpty()) {
            return RoutingDecision.ACCEPT;
        }
        if (issues.stream().anyMatch(i -> i.type() == IssueType.UNSUPPORTED_CASE)) {
            return RoutingDecision.ABORT;
        }
        if (issues.stream().anyMatch(i -> i.type() == IssueType.REQUIREMENTS_CONFLICT)) {
            return RoutingDecision.ESCALATE_HITL;
        }
        if (issues.stream().anyMatch(i -> i.type() == IssueType.MISSING_INFORMATION)) {
            return RoutingDecision.CLARIFY;
        }
        if (issues.stream().allMatch(i -> i.type() == IssueType.LAYOUT_FIXABLE)) {
            return RoutingDecision.REGENERATE;
        }
        return RoutingDecision.ESCALATE_HITL;
    }
}
