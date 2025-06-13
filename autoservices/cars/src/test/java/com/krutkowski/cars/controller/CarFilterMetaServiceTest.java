package com.krutkowski.cars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krutkowski.cars.repository.CarRepository;
import com.krutkowski.cars.services.CarFiltersMetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarFiltersMetaServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarFiltersMetaService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        service = new CarFiltersMetaService(carRepository, objectMapper);
    }

    @Test
    void shouldReturnFiltersMeta() {
        String brandsJson = "[\"Audi\",\"BMW\"]";
        String originsJson = "[\"Germany\",\"USA\"]";
        String bodiesJson = "[\"SUV\",\"Hatchback\"]";
        String colorsJson = "[\"Black\",\"White\"]";
        String fuelsJson = "[\"Diesel\",\"Petrol\"]";

        Object[] queryRow = {brandsJson, originsJson, bodiesJson, colorsJson, fuelsJson};
        List<Object[]> queryResult = new ArrayList<>();
        queryResult.add(queryRow);

        when(carRepository.getCarFiltersMetaRaw()).thenReturn(queryResult);

        Map<String, List<String>> result = service.getFiltersMeta();

        assertThat(result).containsKeys("brands", "originCountries", "bodyTypes", "colors", "fuelTypes");
        assertThat(result.get("brands")).containsExactly("Audi", "BMW");
        assertThat(result.get("originCountries")).containsExactly("Germany", "USA");
        assertThat(result.get("bodyTypes")).containsExactly("SUV", "Hatchback");
        assertThat(result.get("colors")).containsExactly("Black", "White");
        assertThat(result.get("fuelTypes")).containsExactly("Diesel", "Petrol");
    }
}
