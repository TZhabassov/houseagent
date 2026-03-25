package com.example.houseagent.application.validator;

import com.example.houseagent.domain.enums.IssueType;
import com.example.houseagent.domain.enums.Severity;
import com.example.houseagent.domain.model.ConstraintIssue;
import com.example.houseagent.domain.model.HouseRequirements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequirementsValidator {

    public List<ConstraintIssue> validate(HouseRequirements requirements) {
        List<ConstraintIssue> issues = new ArrayList<>();

        if (requirements.totalArea() < 40) {
            issues.add(new ConstraintIssue(IssueType.MISSING_INFORMATION, "Total area must be at least 40m2", List.of(), Severity.HIGH));
        }
        if (requirements.residentsCount() < requirements.bedroomCount()) {
            issues.add(new ConstraintIssue(IssueType.REQUIREMENTS_CONFLICT,
                    "Residents count cannot be less than bedroom count in MVP assumptions", List.of(), Severity.MEDIUM));
        }
        if (requirements.toiletCount() <= 0) {
            issues.add(new ConstraintIssue(IssueType.MISSING_INFORMATION, "At least one toilet is required", List.of(), Severity.HIGH));
        }

        return issues;
    }
}
