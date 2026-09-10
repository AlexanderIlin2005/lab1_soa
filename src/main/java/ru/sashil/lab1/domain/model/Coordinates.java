package ru.sashil.lab1.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Coordinates {
    @NotNull(message = "X cannot be null")
    private Long x;

    @Max(value = 719, message = "Y must be <= 719")
    private long y;

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }
}
