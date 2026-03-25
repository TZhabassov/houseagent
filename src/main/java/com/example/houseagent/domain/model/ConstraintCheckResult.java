package com.example.houseagent.domain.model;

import java.util.List;

public record ConstraintCheckResult(
        boolean valid,
        List<ConstraintIssue> issues) {
}
