package com.krutkowski.cars.repository;

import com.krutkowski.cars.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT DISTINCT c.model FROM Car c WHERE LOWER(c.brand) = LOWER(:brand) ORDER BY c.model")
    List<String> findDistinctModelsByBrandIgnoreCase(@Param("brand") String brand);


}
