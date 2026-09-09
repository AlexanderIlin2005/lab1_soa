package ru.sashil.lab1.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
public class Coordinates {
    @NotNull(message = "X cannot be null")
    private Long x;

    @Max(value = 719, message = "Y must be <= 719")
    private long y;
}