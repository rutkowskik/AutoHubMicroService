package com.krutkowski.cars.model.request;

import jakarta.validation.constraints.*;
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

    @DecimalMin(value = "0.01")
    @Digits(integer = 10, fraction = 2)
    @NotNull(message = "Price cannot be empty")
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
    @NotBlank(message = "Engine cannot be empty")
    private String engine;

    @NotBlank(message = "Fuel type cannot be empty")
    private String fuelType;

}
