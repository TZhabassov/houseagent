package com.example.houseagent.application.workflow;

import com.example.houseagent.domain.enums.ProcessState;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class WorkflowStateMachine {
    private final Map<ProcessState, Set<ProcessState>> transitions = new EnumMap<>(ProcessState.class);

    public WorkflowStateMachine() {
        transitions.put(ProcessState.INIT, EnumSet.of(ProcessState.COLLECT_REQUIREMENTS));
        transitions.put(ProcessState.COLLECT_REQUIREMENTS, EnumSet.of(ProcessState.VALIDATE_REQUIREMENTS));
        transitions.put(ProcessState.VALIDATE_REQUIREMENTS, EnumSet.of(
                ProcessState.CLARIFY_REQUIREMENTS,
                ProcessState.GENERATE_LAYOUT));
        transitions.put(ProcessState.CLARIFY_REQUIREMENTS, EnumSet.of(ProcessState.COLLECT_REQUIREMENTS, ProcessState.NEEDS_CLARIFICATION));
        transitions.put(ProcessState.GENERATE_LAYOUT, EnumSet.of(ProcessState.VALIDATE_LAYOUT));
        transitions.put(ProcessState.VALIDATE_LAYOUT, EnumSet.of(ProcessState.GENERATE_LAYOUT, ProcessState.DONE, ProcessState.NEEDS_CLARIFICATION, ProcessState.ABORTED));
    }

    public boolean canTransition(ProcessState from, ProcessState to) {
        return transitions.getOrDefault(from, Set.of()).contains(to);
    }
}
