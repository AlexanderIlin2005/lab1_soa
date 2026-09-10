package ru.sashil.lab1.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.sashil.lab1.domain.model.Vehicle;
import ru.sashil.lab1.domain.model.VehicleType;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    List<Vehicle> findByNameContainingIgnoreCase(String substring);
    
    List<Vehicle> findByType(VehicleType type);
}