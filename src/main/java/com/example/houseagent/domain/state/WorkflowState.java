package com.example.houseagent.domain.state;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WorkflowState {
    private ProcessState processState;
    private final Instant startedAt;
    private int iteration;
    private List<String> issueFingerprints;

    public WorkflowState() {
        this.processState = ProcessState.INIT;
        this.startedAt = Instant.now();
        this.iteration = 0;
        this.issueFingerprints = new ArrayList<>();
    }

    public ProcessState getProcessState() {
        return processState;
    }

    public void setProcessState(ProcessState processState) {
        this.processState = processState;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public int getIteration() {
        return iteration;
    }

    public void incrementIteration() {
        this.iteration++;
    }

    public List<String> getIssueFingerprints() {
        return issueFingerprints;
    }

    public void setIssueFingerprints(List<String> issueFingerprints) {
        this.issueFingerprints = issueFingerprints;
    }
}
