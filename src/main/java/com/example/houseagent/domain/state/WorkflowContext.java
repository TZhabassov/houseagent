package com.example.houseagent.domain.state;

import com.example.houseagent.domain.enums.PlanningStatus;
import com.example.houseagent.domain.enums.ProcessState;
import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.model.HouseLayout;
import com.example.houseagent.domain.model.HouseRequirements;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WorkflowContext {
    private ProcessState state = ProcessState.INIT;
    private final Instant startedAt = Instant.now();
    private int iterations = 0;
    private HouseRequirements requirements;
    private HouseLayout layout;
    private PlanningStatus status = PlanningStatus.PARTIAL;
    private List<ConstraintIssue> issues = new ArrayList<>();

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public int getIterations() {
        return iterations;
    }

    public void incrementIterations() {
        this.iterations++;
    }

    public HouseRequirements getRequirements() {
        return requirements;
    }

    public void setRequirements(HouseRequirements requirements) {
        this.requirements = requirements;
    }

    public HouseLayout getLayout() {
        return layout;
    }

    public void setLayout(HouseLayout layout) {
        this.layout = layout;
    }

    public PlanningStatus getStatus() {
        return status;
    }

    public void setStatus(PlanningStatus status) {
        this.status = status;
    }

    public List<ConstraintIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<ConstraintIssue> issues) {
        this.issues = new ArrayList<>(issues);
    }
}
