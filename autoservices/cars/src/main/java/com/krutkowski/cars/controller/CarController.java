package com.krutkowski.cars.controller;

import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.services.CarService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {

    private CarService carService;

    //todo implement carDTO

    //todo implement active_state not the delete method

    @GetMapping
    public Page<Car> getAllCars(Pageable page) {
        log.info("Getting all cars");
        return carService.getAllCars(page);
    }

    @GetMapping("/{id}")
    public CarDTO getCarById(@PathVariable("id") Long id) {
        log.info("Getting car by id {}", id);
        return carService.getCarById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCar(@Valid @RequestBody CarRequest carRequest) {
        log.info("Saving car {}", carRequest);
        carService.saveCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCar(@Valid @RequestBody CarRequest carRequest) {
        log.info("Updating car {}", carRequest);
        carService.saveCar(carRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
