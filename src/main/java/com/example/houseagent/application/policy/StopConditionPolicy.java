package com.example.houseagent.application.policy;

import com.example.houseagent.domain.model.ConstraintIssue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class StopConditionPolicy {

    private final int maxIterations;
    private final Duration maxDuration;

    public StopConditionPolicy(@Value("${houseagent.max-iterations:3}") int maxIterations,
                               @Value("${houseagent.max-duration-seconds:30}") long maxDurationSeconds) {
        this.maxIterations = maxIterations;
        this.maxDuration = Duration.ofSeconds(maxDurationSeconds);
    }

    public boolean shouldStop(int iteration,
                              Instant startedAt,
                              List<ConstraintIssue> previousIssues,
                              List<ConstraintIssue> currentIssues) {
        if (iteration >= maxIterations) {
            return true;
        }
        if (Duration.between(startedAt, Instant.now()).compareTo(maxDuration) > 0) {
            return true;
        }
        if (previousIssues != null && currentIssues != null && !previousIssues.isEmpty() && !currentIssues.isEmpty()) {
            if (currentIssues.size() >= previousIssues.size()) {
                return true;
            }
        }
        return false;
    }
}
