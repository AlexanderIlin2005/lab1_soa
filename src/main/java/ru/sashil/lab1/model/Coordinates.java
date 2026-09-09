package ru.sashil.lab1.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Coordinates {
    private Long x; // Not null
    private long y; // Max 719
}