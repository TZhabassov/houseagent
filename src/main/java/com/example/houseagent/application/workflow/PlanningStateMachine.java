package com.example.houseagent.application.workflow;

import com.example.houseagent.domain.state.ProcessState;

import java.util.Map;
import java.util.Set;

public class PlanningStateMachine {

    private static final Map<ProcessState, Set<ProcessState>> TRANSITIONS = Map.of(
            ProcessState.INIT, Set.of(ProcessState.COLLECT_REQUIREMENTS),
            ProcessState.COLLECT_REQUIREMENTS, Set.of(ProcessState.VALIDATE_REQUIREMENTS),
            ProcessState.VALIDATE_REQUIREMENTS, Set.of(ProcessState.CLARIFY_REQUIREMENTS, ProcessState.GENERATE_LAYOUT),
            ProcessState.CLARIFY_REQUIREMENTS, Set.of(ProcessState.NEEDS_CLARIFICATION, ProcessState.VALIDATE_REQUIREMENTS),
            ProcessState.GENERATE_LAYOUT, Set.of(ProcessState.VALIDATE_LAYOUT),
            ProcessState.VALIDATE_LAYOUT, Set.of(ProcessState.GENERATE_LAYOUT, ProcessState.DONE,
                    ProcessState.NEEDS_CLARIFICATION, ProcessState.ABORTED),
            ProcessState.DONE, Set.of(),
            ProcessState.NEEDS_CLARIFICATION, Set.of(),
            ProcessState.ABORTED, Set.of()
    );

    public ProcessState transit(ProcessState from, ProcessState to) {
        Set<ProcessState> allowed = TRANSITIONS.getOrDefault(from, Set.of());
        if (!allowed.contains(to)) {
            throw new IllegalStateException("Illegal transition from " + from + " to " + to);
        }
        return to;
    }
}
