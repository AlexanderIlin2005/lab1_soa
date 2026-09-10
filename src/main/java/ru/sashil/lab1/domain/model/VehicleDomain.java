package ru.sashil.lab1.domain.model;

import ru.sashil.lab1.domain.exception.DomainException;

import java.time.ZonedDateTime;

public record VehicleDomain(
    int id,
    String name,
    Coordinates coordinates,
    ZonedDateTime creationDate,
    float enginePower,
    Integer numberOfWheels,
    VehicleType vehicleType,
    FuelType fuelType
) {
    public VehicleDomain {
        if (id <= 0) {
            throw new DomainException("Id must be positive");
        }

        if (name == null || name.isBlank()) {
            throw new DomainException("Name must be not null and not empty");
        }

        if (coordinates == null) {
            throw new DomainException("Coordinates must be not null");
        }

        if (creationDate == null) {
            throw new DomainException("Creation date must be not null");
        }

        if (enginePower <= 0) {
            throw new DomainException("Engine power must be positive");
        }

        if (numberOfWheels <= 0) {
            throw new DomainException("Number of wheels must be positive");
        }
    }
}
