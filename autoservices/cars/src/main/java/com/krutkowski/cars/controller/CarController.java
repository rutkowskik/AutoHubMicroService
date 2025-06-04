package com.krutkowski.cars.controller;

import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.entity.File;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.services.CarService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @GetMapping()
    public Page<CarDTO> getFilteredCars(
            @RequestParam Map<String, String> filters,
            @RequestParam ("page") int page,
            @RequestParam ("size") int size) {
        return carService.getFilteredCars(filters, page, size);
    }


    @GetMapping("/{id}")
    public CarDTO getCarById(@PathVariable("id") Long id) {
        log.info("Getting car by id {}", id);
        return carService.getCarById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveCar(@Valid @RequestBody CarRequest carRequest) {
        log.info("Saving car {}", carRequest);
        carService.saveCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCar(@Valid @RequestBody CarRequest carRequest) {
        log.info("Updating car {}", carRequest);
        carService.saveCar(carRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/save/images")
    public ResponseEntity<?> saveImages(@RequestParam("files") List<MultipartFile> files){
        if (files == null || files.isEmpty()){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File list is empty");
        }
        List<File> savedFiles = new ArrayList<>();

        files.forEach(file -> savedFiles.add(carService.saveFile(file)));

        return ResponseEntity.ok(savedFiles);
    }

    @PostMapping(value = "/save/images/data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveCarWithFiles(
            @RequestPart("car") @Valid CarRequest carRequest,
            @RequestPart("files") MultipartFile file) {

        log.info("Saving car {}", carRequest);
        carService.saveCarWithFiles(carRequest, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
