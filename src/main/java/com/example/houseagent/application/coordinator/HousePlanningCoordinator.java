package com.example.houseagent.application.coordinator;

import com.example.houseagent.agents.constraints.ConstraintsAgent;
import com.example.houseagent.agents.layout.LayoutAgent;
import com.example.houseagent.agents.requirements.RequirementsAgent;
import com.example.houseagent.application.policy.IssueRoutingPolicy;
import com.example.houseagent.application.policy.StopConditionPolicy;
import com.example.houseagent.application.validator.RequirementsValidator;
import com.example.houseagent.domain.enums.PlanningStatus;
import com.example.houseagent.domain.enums.ProcessState;
import com.example.houseagent.domain.enums.RoutingDecision;
import com.example.houseagent.domain.model.ConstraintCheckResult;
import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.model.HouseLayout;
import com.example.houseagent.domain.model.HouseRequirements;
import com.example.houseagent.domain.model.LayoutRequest;
import com.example.houseagent.domain.model.PlanningResult;
import com.example.houseagent.domain.model.UserRequest;
import com.example.houseagent.domain.state.WorkflowContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HousePlanningCoordinator {
    private static final Logger log = LoggerFactory.getLogger(HousePlanningCoordinator.class);

    private final RequirementsAgent requirementsAgent;
    private final RequirementsValidator requirementsValidator;
    private final LayoutAgent layoutAgent;
    private final ConstraintsAgent constraintsAgent;
    private final StopConditionPolicy stopConditionPolicy;
    private final IssueRoutingPolicy issueRoutingPolicy;

    public HousePlanningCoordinator(RequirementsAgent requirementsAgent,
                                    RequirementsValidator requirementsValidator,
                                    LayoutAgent layoutAgent,
                                    ConstraintsAgent constraintsAgent,
                                    StopConditionPolicy stopConditionPolicy,
                                    IssueRoutingPolicy issueRoutingPolicy) {
        this.requirementsAgent = requirementsAgent;
        this.requirementsValidator = requirementsValidator;
        this.layoutAgent = layoutAgent;
        this.constraintsAgent = constraintsAgent;
        this.stopConditionPolicy = stopConditionPolicy;
        this.issueRoutingPolicy = issueRoutingPolicy;
    }

    public PlanningResult plan(UserRequest userRequest) {
        WorkflowContext context = new WorkflowContext();
        context.setState(ProcessState.COLLECT_REQUIREMENTS);

        HouseRequirements requirements = requirementsAgent.handle(userRequest);
        context.setRequirements(requirements);
        log.info("Requirements collected for totalArea={} bedrooms={}", requirements.totalArea(), requirements.bedroomCount());

        context.setState(ProcessState.VALIDATE_REQUIREMENTS);
        List<ConstraintIssue> requirementIssues = requirementsValidator.validate(requirements);
        if (!requirementIssues.isEmpty()) {
            context.setStatus(PlanningStatus.NEEDS_CLARIFICATION);
            context.setState(ProcessState.NEEDS_CLARIFICATION);
            context.setIssues(requirementIssues);
            return new PlanningResult(requirements, null, context.getStatus(), requirementIssues,
                    "Requirements need clarification before layout generation");
        }

        context.setState(ProcessState.GENERATE_LAYOUT);
        List<ConstraintIssue> previousIssues = List.of();

        while (true) {
            context.incrementIterations();
            HouseLayout layout = layoutAgent.handle(new LayoutRequest(requirements, context.getLayout(), context.getIssues()));
            context.setLayout(layout);
            context.setState(ProcessState.VALIDATE_LAYOUT);

            ConstraintCheckResult checkResult = constraintsAgent.handle(layout);
            List<ConstraintIssue> currentIssues = checkResult.issues();
            context.setIssues(currentIssues);

            if (checkResult.valid()) {
                context.setStatus(PlanningStatus.SUCCESS);
                context.setState(ProcessState.DONE);
                return new PlanningResult(requirements, layout, context.getStatus(), currentIssues, "Layout is valid");
            }

            RoutingDecision decision = issueRoutingPolicy.route(currentIssues);
            log.info("Layout validation issues={}, routingDecision={}, iteration={}", currentIssues.size(), decision, context.getIterations());

            if (stopConditionPolicy.shouldStop(context.getIterations(), context.getStartedAt(), previousIssues, currentIssues)) {
                context.setStatus(PlanningStatus.NEEDS_CLARIFICATION);
                context.setState(ProcessState.NEEDS_CLARIFICATION);
                return new PlanningResult(requirements, layout, context.getStatus(), currentIssues,
                        "Stop condition triggered; escalation to HITL");
            }

            if (decision == RoutingDecision.REGENERATE) {
                previousIssues = currentIssues;
                context.setState(ProcessState.GENERATE_LAYOUT);
                continue;
            }
            if (decision == RoutingDecision.CLARIFY || decision == RoutingDecision.ESCALATE_HITL) {
                context.setStatus(PlanningStatus.NEEDS_CLARIFICATION);
                context.setState(ProcessState.NEEDS_CLARIFICATION);
                return new PlanningResult(requirements, layout, context.getStatus(), currentIssues,
                        "Human clarification required by routing policy");
            }
            context.setStatus(PlanningStatus.FAILED);
            context.setState(ProcessState.ABORTED);
            return new PlanningResult(requirements, layout, context.getStatus(), currentIssues,
                    "Planning aborted by routing policy");
        }
    }
}
