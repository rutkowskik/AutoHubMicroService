package com.krutkowski.cars.model.dto;


import java.math.BigDecimal;


public record CarDTO(
        Long id,
        String brand,
        String model,
        String title,
        String image,
        BigDecimal price,
        int year,
        int mileage,
        int power,
        String type,
        String location,
        String flag,
        String color,
        String engine
) {
}
