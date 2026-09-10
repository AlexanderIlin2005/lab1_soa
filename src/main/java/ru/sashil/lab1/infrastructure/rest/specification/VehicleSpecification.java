package ru.sashil.lab1.infrastructure.rest.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.sashil.lab1.domain.model.Vehicle;
import ru.sashil.lab1.infrastructure.rest.dto.VehicleFilter;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class VehicleSpecification {

    public static Specification<Vehicle> withFilter(VehicleFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.name() != null && !filter.name().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.name() + "%"));
            }
            if (filter.x() != null) {
                predicates.add(criteriaBuilder.equal(root.get("coordinates").get("x"), filter.x()));
            }
            if (filter.y() != null) {
                predicates.add(criteriaBuilder.equal(root.get("coordinates").get("y"), filter.y()));
            }
            if (filter.minEnginePower() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("enginePower"), filter.minEnginePower()));
            }
            if (filter.maxEnginePower() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("enginePower"), filter.maxEnginePower()));
            }
            if (filter.vehicleType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filter.vehicleType()));
            }
            if (filter.fuelType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("fuelType"), filter.fuelType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
