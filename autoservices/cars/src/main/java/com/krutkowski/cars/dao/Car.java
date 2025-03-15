package com.krutkowski.cars.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String brand;
    private String model;
    private String title;
    private String image;
    private int price;
    private int year;
    private int mileage;
    private String power;
    private String type;
    private String location;
    private String flag;
    private String color;
    private String engine;
}

