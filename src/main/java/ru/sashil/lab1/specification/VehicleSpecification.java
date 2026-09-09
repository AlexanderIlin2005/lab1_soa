package ru.sashil.lab1.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.sashil.lab1.dto.VehicleFilter;
import ru.sashil.lab1.model.Vehicle;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class VehicleSpecification {

    public static Specification<Vehicle> withFilter(VehicleFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if (filter.getX() != null) {
                predicates.add(criteriaBuilder.equal(root.get("coordinates").get("x"), filter.getX()));
            }
            if (filter.getY() != null) {
                predicates.add(criteriaBuilder.equal(root.get("coordinates").get("y"), filter.getY()));
            }
            if (filter.getMinEnginePower() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("enginePower"), filter.getMinEnginePower()));
            }
            if (filter.getMaxEnginePower() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("enginePower"), filter.getMaxEnginePower()));
            }
            if (filter.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
            }
            if (filter.getFuelType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("fuelType"), filter.getFuelType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}