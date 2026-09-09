package ru.sashil.lab1.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sashil.lab1.model.Vehicle;
import ru.sashil.lab1.model.VehicleType;
import ru.sashil.lab1.repository.VehicleRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shop") // URL второго сервиса
public class ShopController {

    @Autowired
    private VehicleRepository vehicleRepository;

    // 1. Найти все ТС заданного типа
    @GetMapping("/search/by-type/{type}")
    @Operation(summary = "Find vehicles by type")
    public ResponseEntity<List<Vehicle>> getByType(@PathVariable VehicleType type) {
        List<Vehicle> vehicles = vehicleRepository.findByType(type);
        return ResponseEntity.ok(vehicles);
    }

    // 2. Добавить колёса
    // URL: /add-wheels/{vehicle-id}/number-of-wheels
    // Внимание: в задании URL указан как /add-wheels/{vehicle-id}/number-of-wheels
    // Но обычно число колес передают как параметр или в теле.
    // Исходя из формулировки "добавить ... указанное число колёс", предположим, что число колес - это часть пути или query param.
    // В задании написано: /add-wheels/{vehicle-id}/number-of-wheels. Это странно, так как number-of-wheels выглядит как статическая строка.
    // Скорее всего, имелось в виду: /add-wheels/{vehicle-id}/{wheelsToAdd} или wheelsToAdd в query.
    // Давайте сделаем так: /add-wheels/{id}/{amount}

    @PostMapping("/add-wheels/{id}/{amount}")
    @Operation(summary = "Add wheels to vehicle")
    public ResponseEntity<Vehicle> addWheels(@PathVariable Long id, @PathVariable int amount) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vehicle vehicle = optionalVehicle.get();
        vehicle.setNumberOfWheels(vehicle.getNumberOfWheels() + amount);

        // Проверка на положительность после сложения
        if (vehicle.getNumberOfWheels() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }
}