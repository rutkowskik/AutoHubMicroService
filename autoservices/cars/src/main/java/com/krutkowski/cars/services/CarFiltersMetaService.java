package com.krutkowski.cars.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CarFiltersMetaService {

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    public Map<String, List<String>> getFiltersMeta() {
        Object[] result = (Object[]) entityManager
                .createNativeQuery("""
                    SELECT 
                        json_agg(DISTINCT brand),
                        json_agg(DISTINCT location),
                        json_agg(DISTINCT type),
                        json_agg(DISTINCT color),
                        json_agg(DISTINCT fuel_type)
                    FROM car
                """)
                .getSingleResult();

        try {
            Map<String, List<String>> filters = new HashMap<>();
            filters.put("brands", objectMapper.readValue(result[0].toString(), new TypeReference<>() {}));
            filters.put("originCountries", objectMapper.readValue(result[1].toString(), new TypeReference<>() {}));
            filters.put("bodyTypes", objectMapper.readValue(result[2].toString(), new TypeReference<>() {}));
            filters.put("colors", objectMapper.readValue(result[3].toString(), new TypeReference<>() {}));
            filters.put("fuelTypes", objectMapper.readValue(result[4].toString(), new TypeReference<>() {}));
            return filters;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse filters JSON", e);
        }
    }
}

