package com.krutkowski.cars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krutkowski.cars.dto.CarRequest;
import com.krutkowski.cars.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void saveCar200ForValidRequest() throws Exception {
        CarRequest carRequest = new CarRequest(1L, "BMW", "X5", "Title", "",
                new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                "Katowice", "", "black", "2000 cm3");

        mockMvc.perform(post("/api/v1/cars/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isOk());

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
}