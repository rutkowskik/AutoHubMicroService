package com.krutkowski.cars.controller;

import com.krutkowski.cars.services.CarFiltersMetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarFilterController.class)
class CarFilterControllerTest {

    @MockBean
    private CarFiltersMetaService carFiltersMetaService;

    @Autowired
    private MockMvc mockMvc;

    Map<String, List<String>> filtersMeta;

    @BeforeEach
    void setUp() {
        filtersMeta = new HashMap<>();
        filtersMeta.put("brands",List.of("BMW", "AUDI"));
        filtersMeta.put("originCountries",List.of("Poland", "Germany"));
        filtersMeta.put("bodyTypes",List.of("SUV", "Sedan"));
        filtersMeta.put("colors",List.of("Black", "White"));
        filtersMeta.put("fuelTypes",List.of("Diesel", "Gasoline"));
    }

    @Test
    void getFiltersMeta() throws Exception {
        when(carFiltersMetaService.getFiltersMeta()).thenReturn(filtersMeta);

        mockMvc.perform(get("/api/v1/cars/filters-meta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brands", contains("BMW", "AUDI")))
                .andExpect(jsonPath("$.originCountries", contains("Poland", "Germany")))
                .andExpect(jsonPath("$.bodyTypes", contains("SUV", "Sedan")))
                .andExpect(jsonPath("$.colors", contains("Black", "White")))
                .andExpect(jsonPath("$.fuelTypes", contains("Diesel", "Gasoline")));
    }

    @Test
    void getModelsByBrand() throws Exception {
        String brand = "BMW";
        List<String> models = List.of("X1", "X3");
        when(carFiltersMetaService.findDistinctModelsByBrandIgnoreCase(brand)).thenReturn(models);

        mockMvc.perform(get("/api/v1/cars/models")
                    .param("brand", "BMW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasItems(models.toArray())));
    }
}