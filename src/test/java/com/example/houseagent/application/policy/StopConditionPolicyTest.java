package com.example.houseagent.application.policy;

import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.Severity;
import com.example.houseagent.domain.model.ConstraintIssue;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StopConditionPolicyTest {

    @Test
    void shouldStopOnMaxIterations() {
        StopConditionPolicy policy = new StopConditionPolicy(2, 30);
        boolean stop = policy.shouldStop(2, Instant.now(), List.of(), List.of());
        assertThat(stop).isTrue();
    }

    @Test
    void shouldStopOnNoProgress() {
        StopConditionPolicy policy = new StopConditionPolicy(5, 30);
        var prev = List.of(new ConstraintIssue(IssueType.LAYOUT_FIXABLE, "a", List.of(), Severity.LOW));
        var curr = List.of(
                new ConstraintIssue(IssueType.LAYOUT_FIXABLE, "a", List.of(), Severity.LOW),
                new ConstraintIssue(IssueType.LAYOUT_FIXABLE, "b", List.of(), Severity.LOW)
        );
        assertThat(policy.shouldStop(1, Instant.now(), prev, curr)).isTrue();
    }
}
