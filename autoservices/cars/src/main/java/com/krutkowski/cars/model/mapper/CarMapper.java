package com.krutkowski.cars.model.mapper;

import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.dto.CarDTO;
import org.springframework.stereotype.Component;

@Component
public class CarMapper {

    public CarDTO toDto(Car car) {
        return new CarDTO(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getTitle(),
                car.getImage(),
                car.getPrice(),
                car.getYear(),
                car.getMileage(),
                car.getPower(),
                car.getType(),
                car.getLocation(),
                car.getFlag(),
                car.getColor(),
                car.getEngine()
        );
    }
}
