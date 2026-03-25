package com.example.houseagent.application.policy;

import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.RoutingDecision;
import com.example.houseagent.domain.model.ConstraintIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IssueRoutingPolicy {
    private static final Logger log = LoggerFactory.getLogger(IssueRoutingPolicy.class);

    public RoutingDecision decide(List<ConstraintIssue> issues) {
        if (issues == null || issues.isEmpty()) {
            return RoutingDecision.REGENERATE;
        }

        if (issues.stream().anyMatch(i -> i.type() == IssueType.UNSUPPORTED_CASE)) {
            log.info("Routing to ABORT due to unsupported case");
            return RoutingDecision.ABORT;
        }

        if (issues.stream().anyMatch(i -> i.type() == IssueType.MISSING_INFORMATION
                || i.type() == IssueType.REQUIREMENTS_CONFLICT)) {
            log.info("Routing to REQUEST_CLARIFICATION due to information/conflict issues");
            return RoutingDecision.REQUEST_CLARIFICATION;
        }

        log.info("Routing to REGENERATE for fixable layout issues");
        return RoutingDecision.REGENERATE;
    }
}
