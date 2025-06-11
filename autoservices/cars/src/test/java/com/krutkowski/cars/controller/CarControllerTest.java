package com.krutkowski.cars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                new Car(1L, "BMW", "X5", "Title", "",
                        new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                        "Katowice", "", "black", "2000", "Gasoline", new Date(), new Date()),
                new Car(2L, "AUDI", "Q3", "Title", "",
                        new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                        "Katowice", "", "black", "2000", "Diesel", new Date(), new Date())
        );
    }

    @Test
    void saveCar200ForValidRequest() throws Exception {
        CarRequest carRequest = new CarRequest(1L, "BMW", "X5", "Title", "",
                new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                "Katowice", "", "black", "2000");

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
    void getAllCars() throws Exception {
//        Pageable pageRequest = PageRequest.of(0, 1);
//        Page<Car> page = new PageImpl<>(cars, pageRequest, cars.size());
        mockMvc.perform(get("/api/v1/cars")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(carService, times(1)).getAllCars(any(Pageable.class));
    }

    @Test
    void shouldReturnAllCars() throws Exception {
        // given
        Pageable pageRequest = PageRequest.of(0, 10);
        List<Car> cars = List.of(
                new Car(1L, "BMW", "X5", "Title", "",
                        new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                        "Katowice", "", "black", "2000", "Gasoline",new Date(), new Date()),
                new Car(2L, "AUDI", "Q3", "Title", "",
                        new BigDecimal("20.20"), 1999, 100_000, 150, "Kombi",
                        "Katowice", "", "black", "2000","Diesel", new Date(), new Date())
        );
        Page<Car> page = new PageImpl<>(cars, pageRequest, cars.size());
        when(carService.getAllCars(any(Pageable.class))).thenReturn(page);


        mockMvc.perform(get("/api/v1/cars")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].brand").value("BMW"))
                .andExpect(jsonPath("$.content[1].model").value("Q3"));
    }

    @Test
    public void test() {
        //todo test metody do aktualizowania wpisu
    }

}