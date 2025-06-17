package com.krutkowski.cars.model.mapper;

import com.krutkowski.cars.model.dto.ImageDTO;
import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.entity.Image;
import com.krutkowski.cars.model.request.CarRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarMapper {

    public CarDTO toDto(Car car) {
        return new CarDTO(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getTitle(),
                getMainImageUrl(car.getImages()),
                car.getPrice(),
                car.getYear(),
                car.getMileage(),
                car.getPower(),
                car.getType(),
                car.getLocation(),
                car.getFlag(),
                car.getColor(),
                car.getEngine(),
                car.getFuelType(),
                toImageDtoList(car.getImages())
        );
    }

    private List<ImageDTO> toImageDtoList(List<Image> images) {
        if (images == null) return List.of();

        return images.stream()
                .map(image -> ImageDTO.builder()
                        .name(image.getName())
                        .imageUrl(image.getImageUrl())
                        .created(image.getCreated())
                        .modified(image.getModified())
                        .build()
                ).collect(Collectors.toList());
    }


    private String getMainImageUrl(List<Image> images) {
        return images.stream()
                .filter(Image::getIsMainImage)
                .findFirst()
                .map(Image::getImageUrl)
                .orElse(images.isEmpty() ? null : images.get(0).getImageUrl());
    }

    public Car mapCarFromRequest(@Valid CarRequest carRequest) {
        return Car.builder()
                .id(carRequest.getId())
                .brand(carRequest.getBrand())
                .model(carRequest.getModel())
                .title(carRequest.getTitle())
                .price(carRequest.getPrice())
                .year(carRequest.getYear())
                .mileage(carRequest.getMileage())
                .power(carRequest.getPower())
                .type(carRequest.getType())
                .location(carRequest.getLocation())
                .flag(carRequest.getFlag())
                .color(carRequest.getColor())
                .engine(carRequest.getEngine())
                .fuelType(carRequest.getFuelType())
                .build();
    }
}
