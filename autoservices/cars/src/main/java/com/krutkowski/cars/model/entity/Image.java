package com.krutkowski.cars.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "car_images_info")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private String name;
    private String imageUrl;
    private Date created;
    private Date modified;
    private Boolean isMainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    @JsonBackReference
    private Car car;
}
