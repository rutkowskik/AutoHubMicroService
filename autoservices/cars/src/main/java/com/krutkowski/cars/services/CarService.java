package com.krutkowski.cars.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.krutkowski.cars.config.AwsS3Config;
import com.krutkowski.cars.model.dto.AwsS3SaveImageModel;
import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.entity.File;
import com.krutkowski.cars.model.mapper.CarMapper;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.repository.CarRepository;
import com.krutkowski.cars.repository.FileRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final FileRepo fileRepository;
    private final CarMapper carMapper;
    private final AmazonS3 amazonS3Client;
    private final AwsS3Config amazonS3Config;

    public void saveCar(CarRequest carRequest) {
        Car car = Car.builder()
                .id(carRequest.getId())
                .brand(carRequest.getBrand())
                .model(carRequest.getModel())
                .title(carRequest.getTitle())
                .image(carRequest.getImage())
                .price(carRequest.getPrice())
                .year(carRequest.getYear())
                .mileage(carRequest.getMileage())
                .power(carRequest.getPower())
                .type(carRequest.getType())
                .location(carRequest.getLocation())
                .flag(carRequest.getFlag())
                .color(carRequest.getColor())
                .engine(carRequest.getEngine())
                .build();
        carRepository.save(car);
    }

    public Page<Car> getAllCars(Pageable page) {
        return carRepository.findAll(page);
    }

    public CarDTO getCarById(Long id) {
        return carMapper.toDto(carRepository.findById(id).orElseThrow());
    }

    public File saveFile(MultipartFile file) {
        AwsS3SaveImageModel savedModel = saveImageToAWS(file);

        File fileToSave = File.builder()
                .fileUrl(savedModel.awsUrl())
                .name(savedModel.fileName())
                .build();
        return fileRepository.save(fileToSave);
    }

    private AwsS3SaveImageModel saveImageToAWS(MultipartFile file) {
        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            if (extension != null && !List.of("jpg", "jpeg", "png", "gif").contains(extension.toLowerCase())) {
                throw new IllegalArgumentException("Unsupported file type: " + extension);
            }

            String fileName = UUID.randomUUID() + "." + extension;

            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(amazonS3Config.getBucketName(), fileName, inputStream, objectMetadata);
            amazonS3Client.putObject(putObjectRequest);

            String awsUrl = String.format("https://%s.s3.amazonaws.com/%s",
                    amazonS3Config.getBucketName(),
                    fileName);

            return new AwsS3SaveImageModel(awsUrl, fileName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to server", e);
        }
    }

}
