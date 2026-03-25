package com.example.houseagent.application.coordinator;

import com.example.houseagent.agents.constraints.ConstraintsAgent;
import com.example.houseagent.agents.layout.LayoutAgent;
import com.example.houseagent.agents.requirements.RequirementsAgent;
import com.example.houseagent.application.policy.IssueRoutingPolicy;
import com.example.houseagent.application.policy.StopConditionPolicy;
import com.example.houseagent.application.validator.RequirementsValidator;
import com.example.houseagent.application.workflow.PlanningStateMachine;
import com.example.houseagent.domain.enums.PlanningStatus;
import com.example.houseagent.domain.enums.RoutingDecision;
import com.example.houseagent.domain.model.ConstraintCheckResult;
import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.model.HouseLayout;
import com.example.houseagent.domain.model.HouseRequirements;
import com.example.houseagent.domain.model.LayoutRequest;
import com.example.houseagent.domain.model.PlanningResult;
import com.example.houseagent.domain.model.UserRequest;
import com.example.houseagent.domain.state.ProcessState;
import com.example.houseagent.domain.state.WorkflowState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HousePlanningCoordinator {
    private static final Logger log = LoggerFactory.getLogger(HousePlanningCoordinator.class);

    private final RequirementsAgent requirementsAgent;
    private final RequirementsValidator requirementsValidator;
    private final LayoutAgent layoutAgent;
    private final ConstraintsAgent constraintsAgent;
    private final StopConditionPolicy stopConditionPolicy;
    private final IssueRoutingPolicy issueRoutingPolicy;
    private final PlanningStateMachine stateMachine;

    public HousePlanningCoordinator(
            RequirementsAgent requirementsAgent,
            RequirementsValidator requirementsValidator,
            LayoutAgent layoutAgent,
            ConstraintsAgent constraintsAgent,
            StopConditionPolicy stopConditionPolicy,
            IssueRoutingPolicy issueRoutingPolicy,
            PlanningStateMachine stateMachine
    ) {
        this.requirementsAgent = requirementsAgent;
        this.requirementsValidator = requirementsValidator;
        this.layoutAgent = layoutAgent;
        this.constraintsAgent = constraintsAgent;
        this.stopConditionPolicy = stopConditionPolicy;
        this.issueRoutingPolicy = issueRoutingPolicy;
        this.stateMachine = stateMachine;
    }

    public PlanningResult plan(UserRequest request) {
        WorkflowState workflowState = new WorkflowState();
        HouseRequirements requirements;
        HouseLayout layout = null;

        workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.COLLECT_REQUIREMENTS));
        requirements = requirementsAgent.handle(request);

        workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.VALIDATE_REQUIREMENTS));
        List<ConstraintIssue> requirementIssues = requirementsValidator.validate(requirements);

        if (!requirementIssues.isEmpty()) {
            workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.CLARIFY_REQUIREMENTS));
            workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.NEEDS_CLARIFICATION));
            return new PlanningResult(requirements, null, PlanningStatus.NEEDS_CLARIFICATION,
                    requirementIssues, "Requirements need clarification before layout generation");
        }

        workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.GENERATE_LAYOUT));
        List<ConstraintIssue> issues = List.of();

        while (true) {
            if (stopConditionPolicy.shouldStop(workflowState, issues)) {
                workflowState.setProcessState(ProcessState.ABORTED);
                return new PlanningResult(requirements, layout, PlanningStatus.FAILED, issues,
                        "Stop condition reached");
            }

            LayoutRequest layoutRequest = new LayoutRequest(requirements, layout, issues);
            layout = layoutAgent.handle(layoutRequest);
            workflowState.incrementIteration();

            workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.VALIDATE_LAYOUT));
            ConstraintCheckResult checkResult = constraintsAgent.handle(layout);
            issues = checkResult.issues();

            if (checkResult.valid()) {
                workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.DONE));
                log.info("Planning completed successfully in {} iteration(s)", workflowState.getIteration());
                return new PlanningResult(requirements, layout, PlanningStatus.SUCCESS, issues, "Layout generated");
            }

            RoutingDecision decision = issueRoutingPolicy.decide(issues);
            if (decision == RoutingDecision.REGENERATE) {
                workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.GENERATE_LAYOUT));
                continue;
            }

            if (decision == RoutingDecision.REQUEST_CLARIFICATION) {
                workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.NEEDS_CLARIFICATION));
                return new PlanningResult(requirements, layout, PlanningStatus.NEEDS_CLARIFICATION,
                        issues, "Human clarification required");
            }

            workflowState.setProcessState(stateMachine.transit(workflowState.getProcessState(), ProcessState.ABORTED));
            return new PlanningResult(requirements, layout, PlanningStatus.FAILED, issues,
                    "Unsupported planning case");
        }
    }
}
