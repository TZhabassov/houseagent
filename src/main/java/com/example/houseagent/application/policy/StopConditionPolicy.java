package com.example.houseagent.application.policy;

import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.state.WorkflowState;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class StopConditionPolicy {
    private final int maxIterations;
    private final Duration maxDuration;

    public StopConditionPolicy(int maxIterations, Duration maxDuration) {
        this.maxIterations = maxIterations;
        this.maxDuration = maxDuration;
    }

    public boolean shouldStop(WorkflowState state, List<ConstraintIssue> currentIssues) {
        if (state.getIteration() >= maxIterations) {
            return true;
        }

        if (Duration.between(state.getStartedAt(), Instant.now()).compareTo(maxDuration) > 0) {
            return true;
        }

        List<String> previousFingerprints = state.getIssueFingerprints();
        List<String> currentFingerprints = currentIssues == null
                ? List.of()
                : currentIssues.stream().map(ConstraintIssue::fingerprint).sorted().toList();

        if (!previousFingerprints.isEmpty() && !currentFingerprints.isEmpty()) {
            boolean repeated = previousFingerprints.equals(currentFingerprints);
            boolean noReduction = currentFingerprints.size() >= previousFingerprints.size();
            if (repeated || noReduction) {
                return true;
            }
        }

        state.setIssueFingerprints(currentFingerprints);
        return false;
    }
}
