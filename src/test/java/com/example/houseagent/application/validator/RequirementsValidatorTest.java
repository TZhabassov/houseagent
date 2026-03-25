package com.example.houseagent.application.validator;

import com.example.houseagent.domain.enums.KitchenType;
import com.example.houseagent.domain.enums.LivingAreaType;
import com.example.houseagent.domain.model.HouseRequirements;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequirementsValidatorTest {

    private final RequirementsValidator validator = new RequirementsValidator();

    @Test
    void shouldReturnIssuesForInconsistentRequirements() {
        HouseRequirements requirements = new HouseRequirements(70, 4, 2, 6,
                KitchenType.OPEN_SPACE, LivingAreaType.FAMILY_CENTERED);

        assertFalse(validator.validate(requirements).isEmpty());
    }

    @Test
    void shouldPassValidRequirements() {
        HouseRequirements requirements = new HouseRequirements(140, 3, 4, 2,
                KitchenType.OPEN_SPACE, LivingAreaType.FAMILY_CENTERED);

        assertTrue(validator.validate(requirements).isEmpty());
    }
}
