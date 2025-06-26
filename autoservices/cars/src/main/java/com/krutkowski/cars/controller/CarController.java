package com.krutkowski.cars.controller;

import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.services.CarService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
@CrossOrigin(origins = "http://autohub.kacper.com:3000")
public class CarController {

    private CarService carService;

    //todo implement carDTO

    //todo implement active_state not the delete method

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<CarDTO> getAllCars(
            @RequestParam Map<String, String> filters,
            @RequestParam ("page") int page,
            @RequestParam ("size") int size) {
        return carService.getFilteredCars(filters, page, size);
    }

    @GetMapping("/{id}")
    public CarDTO getCarDetailsById(@PathVariable("id") Long id) {
        log.info("Getting car by id {}", id);
        return carService.getCarById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCarWithoutImage(@Valid @RequestBody CarRequest carRequest) {
        log.info("Saving car without images {}", carRequest);
        carService.saveCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCarWithoutImage(@Valid @RequestBody CarRequest carRequest) {
        log.info("Updating car {}", carRequest);
        carService.saveCar(carRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/save/images/data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveCarWithImages(
            @RequestPart("car") @Valid CarRequest carRequest,
            @RequestPart("files") List<MultipartFile> files) {

        log.info("Saving car with images{}", carRequest);
        carService.saveCarWithFiles(carRequest, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
