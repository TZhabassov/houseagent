package com.example.houseagent.infrastructure.config;

import com.example.houseagent.agents.constraints.ConstraintsAgent;
import com.example.houseagent.agents.layout.LayoutAgent;
import com.example.houseagent.agents.requirements.RequirementsAgent;
import com.example.houseagent.application.coordinator.HousePlanningCoordinator;
import com.example.houseagent.application.policy.IssueRoutingPolicy;
import com.example.houseagent.application.policy.StopConditionPolicy;
import com.example.houseagent.application.validator.RequirementsValidator;
import com.example.houseagent.application.workflow.PlanningStateMachine;
import com.example.houseagent.infrastructure.mock.MockConstraintsAgent;
import com.example.houseagent.infrastructure.mock.MockLayoutAgent;
import com.example.houseagent.infrastructure.mock.MockRequirementsAgent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ApplicationConfig {

    @Bean
    public RequirementsAgent requirementsAgent() {
        return new MockRequirementsAgent();
    }

    @Bean
    public LayoutAgent layoutAgent() {
        return new MockLayoutAgent();
    }

    @Bean
    public ConstraintsAgent constraintsAgent() {
        return new MockConstraintsAgent();
    }

    @Bean
    public RequirementsValidator requirementsValidator() {
        return new RequirementsValidator();
    }

    @Bean
    public StopConditionPolicy stopConditionPolicy() {
        return new StopConditionPolicy(5, Duration.ofSeconds(10));
    }

    @Bean
    public IssueRoutingPolicy issueRoutingPolicy() {
        return new IssueRoutingPolicy();
    }

    @Bean
    public PlanningStateMachine planningStateMachine() {
        return new PlanningStateMachine();
    }

    @Bean
    public HousePlanningCoordinator housePlanningCoordinator(
            RequirementsAgent requirementsAgent,
            RequirementsValidator requirementsValidator,
            LayoutAgent layoutAgent,
            ConstraintsAgent constraintsAgent,
            StopConditionPolicy stopConditionPolicy,
            IssueRoutingPolicy issueRoutingPolicy,
            PlanningStateMachine planningStateMachine
    ) {
        return new HousePlanningCoordinator(
                requirementsAgent,
                requirementsValidator,
                layoutAgent,
                constraintsAgent,
                stopConditionPolicy,
                issueRoutingPolicy,
                planningStateMachine
        );
    }
}
