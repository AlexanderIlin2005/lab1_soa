package ru.sashil.lab1.infrastructure.rest.dto;

import ru.sashil.lab1.domain.model.FuelType;
import ru.sashil.lab1.domain.model.VehicleType;

public record VehicleFilter(
    String name,
    Long x,
    Long y,
    Integer minEnginePower,
    Integer maxEnginePower,
    Integer minNumberOfWheels,
    VehicleType vehicleType,
    FuelType fuelType
) {}
