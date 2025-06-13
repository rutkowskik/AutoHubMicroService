package com.krutkowski.cars.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krutkowski.cars.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CarFiltersMetaService {

    private final CarRepository carRepository;
    private final ObjectMapper objectMapper;

    public Map<String, List<String>> getFiltersMeta() {
        List<Object[]> rawResult = carRepository.getCarFiltersMetaRaw();
        Object[] result = rawResult.get(0);

        try {
            Map<String, List<String>> filters = new HashMap<>();
            filters.put("brands", objectMapper.readValue((String) result[0], new TypeReference<>() {}));
            filters.put("originCountries", objectMapper.readValue((String) result[1], new TypeReference<>() {}));
            filters.put("bodyTypes", objectMapper.readValue((String) result[2], new TypeReference<>() {}));
            filters.put("colors", objectMapper.readValue((String) result[3], new TypeReference<>() {}));
            filters.put("fuelTypes", objectMapper.readValue((String) result[4], new TypeReference<>() {}));
            return filters;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse filters JSON", e);
        }
    }

    public List<String> findDistinctModelsByBrandIgnoreCase(String brand) {
        return carRepository.findDistinctModelsByBrandIgnoreCase(brand);
    }
}

