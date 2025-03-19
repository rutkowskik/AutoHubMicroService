package com.krutkowski.cars.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {

    private Long id;

    @NotBlank(message = "Brand cannot be empty")
    private String brand;

    @NotBlank(message = "Model cannot be empty")
    private String model;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    private String image;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Min(value = 1900, message = "Year must be greater than 1900")
    private int year;

    @Positive(message = "Mileage must be positive")
    private int mileage;

    @Positive(message = "Power must be positive")
    private int power;

    @NotBlank(message = "Type cannot be empty")
    private String type;

    @NotBlank(message = "Location cannot be empty")
    private String location;

    private String flag;

    @NotBlank(message = "Color cannot be empty")
    private String color;

    @Positive(message = "Engine size must be positive")
    private String engine;
}
