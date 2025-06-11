package com.krutkowski.cars.controller;

import com.krutkowski.cars.services.CarFiltersMetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarFilterController {

    private final CarFiltersMetaService filtersMetaService;

    @GetMapping("/filters-meta")
    public ResponseEntity<Map<String, List<String>>> getFiltersMeta() {
        return ResponseEntity.ok(filtersMetaService.getFiltersMeta());
    }
}
