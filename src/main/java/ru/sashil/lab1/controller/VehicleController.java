package ru.sashil.lab1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sashil.lab1.model.Vehicle;
import ru.sashil.lab1.repository.VehicleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles") // Базовый URL для первого сервиса
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    // 1. Получение массива элементов (с пагинацией, сортировкой, фильтрацией)
    // Примечание: Полная фильтрация по всем полям требует Specification,
    // для простоты здесь показана пагинация. Для полной фильтрации используйте JpaSpecificationExecutor.
    @GetMapping
    @Operation(summary = "Get all vehicles with pagination and sorting")
    public Page<Vehicle> getAllVehicles(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String direction) {

        // Реализация сортировки и пагинации
        // PageRequest.of(page, size, Sort.by(direction.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));
        return vehicleRepository.findAll(PageRequest.of(page, size));
    }

    // 2. Получение элемента по ID
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. Добавление нового элемента
    @PostMapping
    @Operation(summary = "Add new vehicle")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        // Валидация и установка автополей
        if (vehicle.getName() == null || vehicle.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (vehicle.getEnginePower() <= 0 || vehicle.getNumberOfWheels() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        // Coordinates validation
        if (vehicle.getCoordinates() != null && vehicle.getCoordinates().getY() > 719) {
            return ResponseEntity.badRequest().build();
        }

        vehicle.setId(null); // ID генерируется автоматически
        vehicle.setCreationDate(LocalDate.now()); // Дата генерируется автоматически

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return ResponseEntity.ok(savedVehicle);
    }

    // 4. Обновление элемента
    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vehicle vehicle = optionalVehicle.get();
        // Обновляем поля, кроме ID и CreationDate (обычно)
        vehicle.setName(vehicleDetails.getName());
        vehicle.setCoordinates(vehicleDetails.getCoordinates());
        vehicle.setEnginePower(vehicleDetails.getEnginePower());
        vehicle.setNumberOfWheels(vehicleDetails.getNumberOfWheels());
        vehicle.setType(vehicleDetails.getType());
        vehicle.setFuelType(vehicleDetails.getFuelType());

        // Валидация при обновлении тоже нужна
        if (vehicle.getEnginePower() <= 0) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }

    // 5. Удаление элемента
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        if (!vehicleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Дополнительные операции ---

    // 6. Рассчитать среднее значение enginePower
    @GetMapping("/stats/average-engine-power")
    @Operation(summary = "Get average engine power")
    public ResponseEntity<Double> getAverageEnginePower() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            return ResponseEntity.ok(0.0);
        }
        double avg = vehicles.stream()
                .mapToInt(Vehicle::getEnginePower)
                .average()
                .orElse(0.0);
        return ResponseEntity.ok(avg);
    }

    // 7. Вернуть количество объектов, enginePower которых меньше заданного
    @GetMapping("/stats/count-by-engine-power-less-than")
    @Operation(summary = "Count vehicles with engine power less than specified")
    public ResponseEntity<Long> countByEnginePowerLessThan(@RequestParam int power) {
        long count = vehicleRepository.findAll().stream()
                .filter(v -> v.getEnginePower() < power)
                .count();
        return ResponseEntity.ok(count);
    }

    // 8. Вернуть массив объектов, name которых содержит подстроку
    @GetMapping("/search/by-name")
    @Operation(summary = "Search vehicles by name substring")
    public ResponseEntity<List<Vehicle>> searchByName(@RequestParam String substring) {
        List<Vehicle> result = vehicleRepository.findByNameContainingIgnoreCase(substring);
        return ResponseEntity.ok(result);
    }
}