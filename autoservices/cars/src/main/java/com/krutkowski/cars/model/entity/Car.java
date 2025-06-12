package com.krutkowski.cars.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car {

    @Id
    @SequenceGenerator(
            name = "car_id_sequence",
            sequenceName = "car_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "car_id_sequence"
    )
    private Long id;

    @NotNull(message = "Brand is required.")
    @Size(min = 1, max = 50, message = "Brand name must be between 1 and 50 characters.")
    private String brand;

    @NotNull(message = "Model is required.")
    @Size(min = 1, max = 50, message = "Model name must be between 1 and 50 characters.")
    private String model;

    @NotNull(message = "Title is required.")
    @Size(min = 1, max = 100, message = "Title name must be between 1 and 100 characters.")
    private String title;

    private String image;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    private BigDecimal price;

    @Min(value = 1900, message = "Year must be after 1900.")
    @Max(value = 2100, message = "Year must be before 2100.")
    private int year;

    @Min(value = 0, message = "Mileage must be greater than or equal to 0.")
    private int mileage;

    @Min(value = 0, message = "Power must be greater than or equal to 0.")
    private int power;

    @NotNull(message = "Type is required.")
    @Size(min = 1, max = 50, message = "Type must be between 1 and 50 characters.")
    private String type;

    @NotNull(message = "Location is required.")
    @Size(min = 1, max = 100, message = "Location must be between 1 and 100 characters.")
    private String location;
    private String flag;

    @NotNull(message = "Color is required.")
    private String color;

    @NotNull(message = "Engine is required.")
    private String engine;

    @NotNull(message = "Fuel type is required.")
    private String fuelType;


    private Date created;

    //todo jakie dodatkowe walidacje - not null default timestamp, update zawsze przy zmianie statusu

    private Date modified;

    //todo active status

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();
}

