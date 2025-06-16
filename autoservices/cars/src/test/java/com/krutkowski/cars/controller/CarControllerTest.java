package com.krutkowski.cars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.mapper.CarMapper;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private ObjectMapper objectMapper;
    List<Car> cars;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
         cars = List.of(
                new Car(1L, "BMW", "X5", "Title",
                        new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                        "Katowice", "", "black", "2000", "Gasoline", new Date(), new Date(), new ArrayList<>()),
                new Car(2L, "AUDI", "Q3", "Title",
                        new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                        "Katowice", "", "black", "2000", "Diesel", new Date(), new Date(), new ArrayList<>())
        );
    }

    @Test
    void saveCar200ForValidRequest() throws Exception {
        CarRequest carRequest = new CarRequest(1L, "BMW", "X5", "Title",
                new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                "Katowice", "", "black", "2000", "fuel_type");

        mockMvc.perform(post("/api/v1/cars/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isCreated());

        verify(carService, times(1)).saveCar(carRequest);
    }

    @Test
    void saveCar400MissingRequestAttribute() throws Exception {
        CarRequest carRequest = new CarRequest();

        mockMvc.perform(post("/api/v1/cars/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isBadRequest());

        verify(carService, never()).saveCar(any());
    }

    @Test
    void shouldReturnAllCars() throws Exception {
        // given
        List<Car> cars = List.of(
                new Car(1L, "BMW", "X5", "Title",
                        new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                        "Katowice", "", "black", "2000", "Gasoline",new Date(), new Date(), new ArrayList<>()),
                new Car(2L, "AUDI", "Q3", "Title",
                        new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                        "Katowice", "", "black", "2000","Diesel", new Date(), new Date(), new ArrayList<>())
        );
        CarMapper carMapper = new CarMapper();
        List<CarDTO> DTOs = cars.stream().map(carMapper::toDto).collect(Collectors.toList());
        Page<CarDTO> page = new PageImpl<>(DTOs);
        when(carService.getFilteredCars(anyMap(), eq(0), eq(10))).thenReturn(page);


        mockMvc.perform(get("/api/v1/cars")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].brand").value("BMW"))
                .andExpect(jsonPath("$.content[1].brand").value("AUDI"));

    }

    @Test
    void updateCarShouldReturn200ForValidRequest() throws Exception {
        Long carId = 2L;
        CarRequest carRequest = new CarRequest(carId, "BMW", "X5", "Updated title",
                new BigDecimal("25.00"), 2005, 120_000, 160, "SUV",
                "Warszawa", "", "white", "2500", "Diesel");

        mockMvc.perform(post("/api/v1/cars/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isOk());

        verify(carService, times(1)).saveCar(carRequest);
    }

    @Test
    void updateCarShouldReturn400ForInvalidRequest() throws Exception {
        Long carId = 2L;
        CarRequest emptyRequest = new CarRequest(); // pusty request

        mockMvc.perform(put("/api/v1/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest());

        verify(carService, never()).saveCar(any());
    }



}