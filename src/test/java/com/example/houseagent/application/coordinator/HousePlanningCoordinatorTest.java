package com.example.houseagent.application.coordinator;

import com.example.houseagent.agents.constraints.ConstraintsAgent;
import com.example.houseagent.agents.layout.LayoutAgent;
import com.example.houseagent.agents.requirements.RequirementsAgent;
import com.example.houseagent.application.policy.IssueRoutingPolicy;
import com.example.houseagent.application.policy.StopConditionPolicy;
import com.example.houseagent.application.validator.RequirementsValidator;
import com.example.houseagent.application.workflow.PlanningStateMachine;
import com.example.houseagent.domain.enums.KitchenType;
import com.example.houseagent.domain.enums.LivingAreaType;
import com.example.houseagent.domain.enums.PlanningStatus;
import com.example.houseagent.domain.model.ConstraintCheckResult;
import com.example.houseagent.domain.model.HouseLayout;
import com.example.houseagent.domain.model.HouseRequirements;
import com.example.houseagent.domain.model.PlanningResult;
import com.example.houseagent.domain.model.UserRequest;
import com.example.houseagent.infrastructure.mock.MockConstraintsAgent;
import com.example.houseagent.infrastructure.mock.MockLayoutAgent;
import com.example.houseagent.infrastructure.mock.MockRequirementsAgent;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HousePlanningCoordinatorTest {

    @Test
    void shouldReturnSuccessForTypicalRequest() {
        RequirementsAgent requirementsAgent = new MockRequirementsAgent();
        LayoutAgent layoutAgent = new MockLayoutAgent();
        ConstraintsAgent constraintsAgent = new MockConstraintsAgent();

        HousePlanningCoordinator coordinator = new HousePlanningCoordinator(
                requirementsAgent,
                new RequirementsValidator(),
                layoutAgent,
                constraintsAgent,
                new StopConditionPolicy(5, Duration.ofSeconds(10)),
                new IssueRoutingPolicy(),
                new PlanningStateMachine()
        );

        PlanningResult result = coordinator.plan(new UserRequest(120, 3, 4, 2,
                KitchenType.OPEN_SPACE, LivingAreaType.FAMILY_CENTERED));

        assertEquals(PlanningStatus.SUCCESS, result.status());
    }

    @Test
    void shouldRequestClarificationOnRequirementConflicts() {
        RequirementsAgent requirementsAgent = new MockRequirementsAgent();
        LayoutAgent layoutAgent = new MockLayoutAgent();
        ConstraintsAgent constraintsAgent = (HouseLayout layout) -> new ConstraintCheckResult(true, List.of());

        HousePlanningCoordinator coordinator = new HousePlanningCoordinator(
                requirementsAgent,
                new RequirementsValidator(),
                layoutAgent,
                constraintsAgent,
                new StopConditionPolicy(5, Duration.ofSeconds(10)),
                new IssueRoutingPolicy(),
                new PlanningStateMachine()
        );

        PlanningResult result = coordinator.plan(new UserRequest(70, 4, 2, 1,
                KitchenType.OPEN_SPACE, LivingAreaType.FAMILY_CENTERED));

        assertEquals(PlanningStatus.NEEDS_CLARIFICATION, result.status());
    }
}
