package com.krutkowski.cars.repository;

import com.krutkowski.cars.dao.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

}
