package com.example.houseagent.application.validator;

import com.example.houseagent.domain.enums.KitchenType;
import com.example.houseagent.domain.enums.LivingAreaType;
import com.example.houseagent.domain.model.HouseRequirements;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequirementsValidatorTest {

    private final RequirementsValidator validator = new RequirementsValidator();

    @Test
    void shouldReturnIssuesForInvalidRequirements() {
        HouseRequirements requirements = new HouseRequirements(30, 3, 2, 0, KitchenType.CLOSED, LivingAreaType.BALANCED);

        var issues = validator.validate(requirements);

        assertThat(issues).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldReturnNoIssuesForValidRequirements() {
        HouseRequirements requirements = new HouseRequirements(120, 3, 4, 2, KitchenType.OPEN_SPACE, LivingAreaType.FAMILY_CENTERED);

        var issues = validator.validate(requirements);

        assertThat(issues).isEmpty();
    }
}
