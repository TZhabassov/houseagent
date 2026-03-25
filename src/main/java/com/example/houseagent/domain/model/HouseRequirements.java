package com.example.houseagent.domain.model;

import com.example.houseagent.domain.enums.KitchenType;
import com.example.houseagent.domain.enums.LivingAreaType;

public record HouseRequirements(
        int totalArea,
        int bedroomCount,
        int residentsCount,
        int toiletCount,
        KitchenType kitchenType,
        LivingAreaType livingAreaType
) {
}
