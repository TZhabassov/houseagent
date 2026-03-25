package com.example.houseagent.domain.model;

import com.example.houseagent.domain.enums.KitchenType;
import com.example.houseagent.domain.enums.LivingAreaType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @Min(40) int totalArea,
        @Min(1) int bedroomCount,
        @Min(1) int residentsCount,
        @Min(1) int toiletCount,
        @NotNull KitchenType kitchenType,
        @NotNull LivingAreaType livingAreaType) {
}
