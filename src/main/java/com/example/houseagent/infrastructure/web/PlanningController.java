package com.example.houseagent.infrastructure.web;

import com.example.houseagent.application.coordinator.HousePlanningCoordinator;
import com.example.houseagent.domain.model.PlanningResult;
import com.example.houseagent.domain.model.UserRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/planning")
public class PlanningController {

    private final HousePlanningCoordinator coordinator;

    public PlanningController(HousePlanningCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @PostMapping
    public PlanningResult plan(@Valid @RequestBody UserRequest request) {
        return coordinator.plan(request);
    }
}
