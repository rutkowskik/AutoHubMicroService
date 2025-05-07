package com.krutkowski.cars.controller;

import com.krutkowski.cars.dao.Car;
import com.krutkowski.cars.dto.CarRequest;
import com.krutkowski.cars.services.CarService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //todo implement carDTO

    @GetMapping
    public Page<Car> getAllCars(Pageable page) {
        log.info("Getting all cars");
        return carService.getAllCars(page);
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable("id") Long id) {
        log.info("Getting car by id {}", id);
        return carService.getCarById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCar(@Valid @RequestBody CarRequest carRequest) {
        log.info("Saving car {}", carRequest);
        carService.saveCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
