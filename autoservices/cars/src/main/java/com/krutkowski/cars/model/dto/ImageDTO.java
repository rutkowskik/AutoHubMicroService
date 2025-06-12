package com.krutkowski.cars.model.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record ImageDTO (
        String name,
        String imageUrl,
        Date created,
        Date modified

){
}
