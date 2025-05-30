package com.krutkowski.cars.repository;

import com.krutkowski.cars.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

}
