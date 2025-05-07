package com.krutkowski.cars.controller;

import com.krutkowski.cars.dao.Car;
import com.krutkowski.cars.dto.CarRequest;
import com.krutkowski.cars.services.CarService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {

    private CarService carService;

    @GetMapping
    public List<Car> getAllCars() {
        log.info("Getting all cars");
        return carService.getAllCars();
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCar(@Valid @RequestBody CarRequest carRequest) {
        log.info("Saving car {}", carRequest);
        carService.saveCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
