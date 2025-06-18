package com.krutkowski.cars.controller;

import com.krutkowski.cars.services.CarFiltersMetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarFilterController {

    private final CarFiltersMetaService filtersMetaService;

    @GetMapping("/filters-meta")
    public ResponseEntity<Map<String, List<String>>> getFiltersMeta() {
        Map<String, List<String>> filtersMeta = filtersMetaService.getFiltersMeta();
        log.info("getFiltersMeta{}", filtersMeta);
        return ResponseEntity.ok(filtersMeta);
    }

    @GetMapping("/models")
    public List<String> getModelsByBrand(@RequestParam("brand") String brand) {
        List<String> distinctModelsByBrandIgnoreCase = filtersMetaService.findDistinctModelsByBrandIgnoreCase(brand);
        log.info("getModelsByBrand {}", brand, distinctModelsByBrandIgnoreCase);
        return distinctModelsByBrandIgnoreCase;
    }
}
