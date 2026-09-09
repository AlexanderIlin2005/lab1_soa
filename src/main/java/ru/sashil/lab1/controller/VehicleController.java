package ru.sashil.lab1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sashil.lab1.model.Vehicle;
import ru.sashil.lab1.model.FuelType;
import ru.sashil.lab1.model.VehicleType;
import ru.sashil.lab1.repository.VehicleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ru.sashil.lab1.dto.VehicleFilter;

import org.springframework.data.jpa.domain.Specification;
import ru.sashil.lab1.specification.VehicleSpecification;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/vehicles") 
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    
    
    
    @GetMapping
    @Operation(summary = "Get all vehicles with filtering, sorting and pagination")
    public Page<Vehicle> getAllVehicles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long x,
            @RequestParam(required = false) Long y,
            @RequestParam(required = false) Integer minEnginePower,
            @RequestParam(required = false) Integer maxEnginePower,
            @RequestParam(required = false) VehicleType type,
            @RequestParam(required = false) FuelType fuelType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        
        VehicleFilter filter = new VehicleFilter();
        filter.setName(name);
        filter.setX(x);
        filter.setY(y);
        filter.setMinEnginePower(minEnginePower);
        filter.setMaxEnginePower(maxEnginePower);
        filter.setType(type);
        filter.setFuelType(fuelType);

        
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(dir, sortBy);
        }

        
        Specification<Vehicle> spec = VehicleSpecification.withFilter(filter);
        return vehicleRepository.findAll(spec, PageRequest.of(page, size, sort));
    }

    
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @PostMapping
    @Operation(summary = "Add new vehicle")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        
        if (vehicle.getName() == null || vehicle.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (vehicle.getEnginePower() <= 0 || vehicle.getNumberOfWheels() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        
        if (vehicle.getCoordinates() != null && vehicle.getCoordinates().getY() > 719) {
            return ResponseEntity.badRequest().build();
        }

        vehicle.setId(null); 
        vehicle.setCreationDate(LocalDate.now()); 

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return ResponseEntity.ok(savedVehicle);
    }

    
    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vehicle vehicle = optionalVehicle.get();
        
        vehicle.setName(vehicleDetails.getName());
        vehicle.setCoordinates(vehicleDetails.getCoordinates());
        vehicle.setEnginePower(vehicleDetails.getEnginePower());
        vehicle.setNumberOfWheels(vehicleDetails.getNumberOfWheels());
        vehicle.setType(vehicleDetails.getType());
        vehicle.setFuelType(vehicleDetails.getFuelType());

        
        if (vehicle.getEnginePower() <= 0) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }

    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        if (!vehicleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    

    
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

    
    @GetMapping("/stats/count-by-engine-power-less-than")
    @Operation(summary = "Count vehicles with engine power less than specified")
    public ResponseEntity<Long> countByEnginePowerLessThan(@RequestParam int power) {
        long count = vehicleRepository.findAll().stream()
                .filter(v -> v.getEnginePower() < power)
                .count();
        return ResponseEntity.ok(count);
    }

    
    @GetMapping("/search/by-name")
    @Operation(summary = "Search vehicles by name substring")
    public ResponseEntity<List<Vehicle>> searchByName(@RequestParam String substring) {
        List<Vehicle> result = vehicleRepository.findByNameContainingIgnoreCase(substring);
        return ResponseEntity.ok(result);
    }
}