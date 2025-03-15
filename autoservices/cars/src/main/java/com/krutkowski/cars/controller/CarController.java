package com.krutkowski.cars.controller;

import com.krutkowski.cars.dto.CarRequest;
import com.krutkowski.cars.services.CarService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {

    private CarService carService;

    @PostMapping("/save")
    public void saveCar(@Valid @RequestBody CarRequest carRequest) {
        log.info("Saving car {}", carRequest);
        carService.saveCar(carRequest);
    }
}
