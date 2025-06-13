package com.krutkowski.cars.model.dto;


import java.math.BigDecimal;
import java.util.List;


public record CarDTO(
        Long id,
        String brand,
        String model,
        String title,
        String mainImage,
        BigDecimal price,
        int year,
        int mileage,
        int power,
        String type,
        String location,
        String flag,
        String color,
        String engine,
        String fuelType,
        List<ImageDTO> images
) {
}
