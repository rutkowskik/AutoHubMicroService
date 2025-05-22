package com.krutkowski.cars.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.entity.File;
import com.krutkowski.cars.model.mapper.CarMapper;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.repository.CarRepository;
import com.krutkowski.cars.repository.FileRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final FileRepo fileRepository;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, FileRepo fileRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.fileRepository = fileRepository;
        this.carMapper = carMapper;
    }

    @Value("${amazon.aws.access-key-id}")
    private String awsS3AccessKey;

    @Value("${amazon.aws.access-key-secret}")
    private String awsS3SecretKey;

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

    private String saveImageToAWS(MultipartFile file) {
        try {
            String s3FileName = file.getOriginalFilename();

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
            AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_WEST_1)
                    .build();

            InputStream inputStream = file.getInputStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();

            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType("image/jpeg");
            String bucketName = "car-service-photos";
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, objectMetadata);
            s3client.putObject(putObjectRequest);
            return "https://" + bucketName + ".s3.amazonaws.com/" + s3FileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public File saveFile(MultipartFile file, String name) {
        String saveFileURl = saveImageToAWS(file);

        File fileToSave = File.builder()
                .fileUrl(saveFileURl)
                .name(name)
                .build();
        return fileRepository.save(fileToSave);
    }
}
