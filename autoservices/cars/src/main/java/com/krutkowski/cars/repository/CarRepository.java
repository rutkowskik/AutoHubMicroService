package com.krutkowski.cars.repository;

import com.krutkowski.cars.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT DISTINCT c.model FROM Car c WHERE LOWER(c.brand) = LOWER(:brand) ORDER BY c.model")
    List<String> findDistinctModelsByBrandIgnoreCase(@Param("brand") String brand);


    @Query(value = """
        SELECT 
            json_agg(DISTINCT brand),
            json_agg(DISTINCT location),
            json_agg(DISTINCT type),
            json_agg(DISTINCT color),
            json_agg(DISTINCT fuel_type)
        FROM car
        """, nativeQuery = true)
    List<Object[]> getCarFiltersMetaRaw();
}
