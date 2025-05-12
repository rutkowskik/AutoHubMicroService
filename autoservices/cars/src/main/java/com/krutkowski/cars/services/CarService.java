package com.krutkowski.cars.services;

import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.mapper.CarMapper;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public void saveCar(CarRequest carRequest) {
        Car car = Car.builder()
                .id(carRequest.getId())
                .brand(carRequest.getBrand())
                .model(carRequest.getModel())
                .title(carRequest.getTitle())
                .image(carRequest.getImage())
                .price(carRequest.getPrice())
                .year(carRequest.getYear())
                .mileage(carRequest.getMileage())
                .power(carRequest.getPower())
                .type(carRequest.getType())
                .location(carRequest.getLocation())
                .flag(carRequest.getFlag())
                .color(carRequest.getColor())
                .engine(carRequest.getEngine())
                .build();
        carRepository.save(car);
    }

    public Page<Car> getAllCars(Pageable page) {
        return carRepository.findAll(page);
    }

    public CarDTO getCarById(Long id) {
        return carMapper.toDto(carRepository.findById(id).orElseThrow());
    }
}
