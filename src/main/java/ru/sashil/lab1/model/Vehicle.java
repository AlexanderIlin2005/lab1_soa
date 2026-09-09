package ru.sashil.lab1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Coordinates coordinates;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(name = "engine_power", nullable = false)
    private int enginePower;

    @Column(name = "number_of_wheels", nullable = false)
    private int numberOfWheels;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
}