package ru.sashil.lab1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.sashil.lab1.model.Vehicle;
import ru.sashil.lab1.model.VehicleType;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    // Для операции: Вернуть массив объектов, значение поля name которых содержит заданную подстроку
    List<Vehicle> findByNameContainingIgnoreCase(String substring);

    // Для операции: Найти все транспортные средства заданного типа (для второго сервиса)
    List<Vehicle> findByType(VehicleType type);
}