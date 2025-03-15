package com.krutkowski.cars.services;

import com.krutkowski.cars.dao.Car;
import com.krutkowski.cars.dto.CarRequest;
import com.krutkowski.cars.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    public void saveCar(CarRequest carRequest) {
        Car car = Car.builder()
                .id(carRequest.getId())
                .brand(carRequest.getBrand())
                .model(carRequest.getModel())
                .build();
        //todo add other properties
        carRepository.save(car);
    }
}
