package com.example.houseagent.application.coordinator;

import com.example.houseagent.agents.constraints.ConstraintsAgent;
import com.example.houseagent.agents.layout.LayoutAgent;
import com.example.houseagent.agents.requirements.RequirementsAgent;
import com.example.houseagent.application.policy.IssueRoutingPolicy;
import com.example.houseagent.application.policy.StopConditionPolicy;
import com.example.houseagent.application.validator.RequirementsValidator;
import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.KitchenType;
import com.example.houseagent.domain.enums.LivingAreaType;
import com.example.houseagent.domain.enums.PlanningStatus;
import com.example.houseagent.domain.enums.RoomSizeCategory;
import com.example.houseagent.domain.enums.RoomType;
import com.example.houseagent.domain.enums.Severity;
import com.example.houseagent.domain.enums.ZoneType;
import com.example.houseagent.domain.model.ConstraintCheckResult;
import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.model.HouseLayout;
import com.example.houseagent.domain.model.HouseRequirements;
import com.example.houseagent.domain.model.RoomConnection;
import com.example.houseagent.domain.model.RoomPlan;
import com.example.houseagent.domain.model.UserRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HousePlanningCoordinatorTest {

    @Test
    void shouldReturnSuccessForValidFlow() {
        RequirementsAgent requirementsAgent = input -> new HouseRequirements(120, 3, 4, 2, KitchenType.OPEN_SPACE, LivingAreaType.FAMILY_CENTERED);
        LayoutAgent layoutAgent = input -> new HouseLayout(
                List.of(new RoomPlan("Living Room", RoomType.LIVING_ROOM, RoomSizeCategory.LARGE, ZoneType.SOCIAL)),
                List.of(new RoomConnection("Living Room", "Kitchen", "OPEN_PASSAGE"))
        );
        ConstraintsAgent constraintsAgent = input -> new ConstraintCheckResult(true, List.of());

        HousePlanningCoordinator coordinator = new HousePlanningCoordinator(
                requirementsAgent,
                new RequirementsValidator(),
                layoutAgent,
                constraintsAgent,
                new StopConditionPolicy(3, 30),
                new IssueRoutingPolicy()
        );

        var result = coordinator.plan(new UserRequest(120, 3, 4, 2, KitchenType.OPEN_SPACE, LivingAreaType.FAMILY_CENTERED));

        assertThat(result.status()).isEqualTo(PlanningStatus.SUCCESS);
    }

    @Test
    void shouldEscalateWhenRequirementsInvalid() {
        RequirementsAgent requirementsAgent = input -> new HouseRequirements(30, 4, 2, 0, KitchenType.CLOSED, LivingAreaType.BALANCED);
        LayoutAgent layoutAgent = input -> null;
        ConstraintsAgent constraintsAgent = input -> new ConstraintCheckResult(false,
                List.of(new ConstraintIssue(IssueType.MISSING_INFORMATION, "x", List.of(), Severity.HIGH)));

        HousePlanningCoordinator coordinator = new HousePlanningCoordinator(
                requirementsAgent,
                new RequirementsValidator(),
                layoutAgent,
                constraintsAgent,
                new StopConditionPolicy(3, 30),
                new IssueRoutingPolicy()
        );

        var result = coordinator.plan(new UserRequest(30, 4, 2, 0, KitchenType.CLOSED, LivingAreaType.BALANCED));

        assertThat(result.status()).isEqualTo(PlanningStatus.NEEDS_CLARIFICATION);
    }
}
