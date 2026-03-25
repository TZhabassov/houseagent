package com.example.houseagent.application.validator;

import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.Severity;
import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.model.HouseRequirements;

import java.util.ArrayList;
import java.util.List;

public class RequirementsValidator {

    public List<ConstraintIssue> validate(HouseRequirements requirements) {
        List<ConstraintIssue> issues = new ArrayList<>();

        if (requirements.totalArea() < 30) {
            issues.add(new ConstraintIssue(IssueType.MISSING_INFORMATION,
                    "Total area is too small for a single-floor house concept",
                    List.of(), Severity.HIGH));
        }

        if (requirements.bedroomCount() > requirements.residentsCount() + 2) {
            issues.add(new ConstraintIssue(IssueType.REQUIREMENTS_CONFLICT,
                    "Bedroom count is unusually high versus residents",
                    List.of(), Severity.MEDIUM));
        }

        if (requirements.toiletCount() > requirements.bedroomCount() + 2) {
            issues.add(new ConstraintIssue(IssueType.REQUIREMENTS_CONFLICT,
                    "Toilet count is inconsistent with bedroom count",
                    List.of(), Severity.MEDIUM));
        }

        if (requirements.totalArea() < (requirements.bedroomCount() * 15 + 20)) {
            issues.add(new ConstraintIssue(IssueType.REQUIREMENTS_CONFLICT,
                    "Requested bedroom count likely does not fit in total area",
                    List.of(), Severity.HIGH));
        }

        return issues;
    }
}
