package ru.sashil.lab1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty or null")
    private String name;

    @NotNull(message = "Coordinates cannot be null")
    @Embedded
    private Coordinates coordinates;

    // CreationDate генерируется автоматически, при вводе его можно игнорировать или не принимать
    private LocalDate creationDate;

    @Min(value = 1, message = "Engine power must be > 0")
    private int enginePower;

    @Min(value = 1, message = "Number of wheels must be > 0")
    private int numberOfWheels;

    private VehicleType type;
    private FuelType fuelType;
}