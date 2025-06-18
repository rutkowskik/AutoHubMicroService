package com.krutkowski.cars.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krutkowski.cars.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarFiltersMetaServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarFiltersMetaService carFiltersMetaService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(carFiltersMetaService, "objectMapper", new ObjectMapper());
    }

    @Test
    void shouldReturnParsedFiltersMeta() {
        // given
        String brandsJson = "[\"BMW\", \"Audi\"]";
        String countriesJson = "[\"Poland\", \"Germany\"]";
        String bodyTypesJson = "[\"SUV\", \"Sedan\"]";
        String colorsJson = "[\"Black\", \"White\"]";
        String fuelTypesJson = "[\"Diesel\", \"Gasoline\"]";

        Object[] rawRow = {brandsJson, countriesJson, bodyTypesJson, colorsJson, fuelTypesJson};
        List<Object[]> rawResult = Collections.singletonList(rawRow);

        when(carRepository.getCarFiltersMetaRaw()).thenReturn(rawResult);

        // when
        Map<String, List<String>> filtersMeta = carFiltersMetaService.getFiltersMeta();

        // then
        assertThat(filtersMeta).isNotNull();
        assertThat(filtersMeta.get("brands")).containsExactly("BMW", "Audi");
        assertThat(filtersMeta.get("originCountries")).containsExactly("Poland", "Germany");
        assertThat(filtersMeta.get("bodyTypes")).containsExactly("SUV", "Sedan");
        assertThat(filtersMeta.get("colors")).containsExactly("Black", "White");
        assertThat(filtersMeta.get("fuelTypes")).containsExactly("Diesel", "Gasoline");
    }

    @Test
    void findDistinctModelsByBrandIgnoreCase() {
        //given
        String brand = "BMW";
        when(carRepository.findDistinctModelsByBrandIgnoreCase(brand)).thenReturn(List.of("X1", "X3", "X5", "X7"));

        //when
        List<String> cars = carFiltersMetaService.findDistinctModelsByBrandIgnoreCase(brand);

        //then
        assertNotNull(cars);
        assertEquals(4, cars.size());
        assertEquals("X1", cars.get(0));
        assertEquals("X3", cars.get(1));
        assertEquals("X5", cars.get(2));
        assertEquals("X7", cars.get(3));
        verify(carRepository, times(1)).findDistinctModelsByBrandIgnoreCase(brand);
    }
}