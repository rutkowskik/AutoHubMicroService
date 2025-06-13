package com.krutkowski.cars.repository;

import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.entity.File;
import com.krutkowski.cars.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ImageRepo extends JpaRepository<Image, Long> {

    List<Image> findAllByCarAndIsMainImageTrue(Car car);

}
