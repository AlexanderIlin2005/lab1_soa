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
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping("/search/by-type/{type}")
    @Operation(summary = "Find vehicles by type")
    public ResponseEntity<List<Vehicle>> getByType(@PathVariable VehicleType type) {
        List<Vehicle> vehicles = vehicleRepository.findByType(type);
        return ResponseEntity.ok(vehicles);
    }

    
    
    
    

    @PostMapping("/add-wheels/{id}/{wheelsAmount}")
    @Operation(summary = "Add wheels to vehicle")
    public ResponseEntity<Vehicle> addWheels(@PathVariable Long id, @PathVariable int wheelsAmount) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vehicle vehicle = optionalVehicle.get();
        vehicle.setNumberOfWheels(vehicle.getNumberOfWheels() + wheelsAmount);

        
        if (vehicle.getNumberOfWheels() <= 0) {
            return ResponseEntity.badRequest().body(vehicle); 
        }

        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }
}