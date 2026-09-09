package ru.sashil.lab1.dto;

import lombok.Data;
import ru.sashil.lab1.model.FuelType;
import ru.sashil.lab1.model.VehicleType;

@Data
public class VehicleFilter {
    private String name;
    private Long x;
    private Long y; // В задании y - long, но для фильтрации удобнее Long
    private Integer minEnginePower;
    private Integer maxEnginePower;
    private Integer minNumberOfWheels;
    private VehicleType type;
    private FuelType fuelType;
}