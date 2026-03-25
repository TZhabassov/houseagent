package com.example.houseagent.application.policy;

import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.Severity;
import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.state.WorkflowState;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StopConditionPolicyTest {

    @Test
    void shouldStopWhenMaxIterationsReached() {
        StopConditionPolicy policy = new StopConditionPolicy(1, Duration.ofSeconds(30));
        WorkflowState state = new WorkflowState();
        state.incrementIteration();

        assertTrue(policy.shouldStop(state, List.of()));
    }

    @Test
    void shouldStopOnNoProgressRepeatedIssues() {
        StopConditionPolicy policy = new StopConditionPolicy(5, Duration.ofSeconds(30));
        WorkflowState state = new WorkflowState();
        List<ConstraintIssue> issues = List.of(new ConstraintIssue(
                IssueType.LAYOUT_FIXABLE, "same", List.of("Kitchen"), Severity.MEDIUM
        ));

        assertFalse(policy.shouldStop(state, issues));
        assertTrue(policy.shouldStop(state, issues));
    }
}
