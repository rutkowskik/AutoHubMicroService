package com.krutkowski.cars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krutkowski.cars.services.CarFiltersMetaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarFiltersMetaServiceTest {

    @Mock
    EntityManager entityManager;

    @InjectMocks
    CarFiltersMetaService service;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        service = new CarFiltersMetaService(entityManager, objectMapper);
    }

    @Test
    void shouldReturnFiltersMeta() {
        String brandsJson = "[\"Audi\",\"BMW\"]";
        String originsJson = "[\"Germany\",\"USA\"]";
        String bodiesJson = "[\"SUV\",\"Hatchback\"]";

        Object[] queryResult = {brandsJson, originsJson, bodiesJson};

        Query mockQuery = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(queryResult);

        Map<String, List<String>> result = service.getFiltersMeta();

        assertThat(result).containsKeys("brands", "originCountries", "bodyTypes");
        assertThat(result.get("brands")).containsExactly("Audi", "BMW");
        assertThat(result.get("originCountries")).containsExactly("Germany", "USA");
        assertThat(result.get("bodyTypes")).containsExactly("SUV", "Hatchback");
    }
}

