package com.krutkowski.cars.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.krutkowski.cars.config.AwsS3Config;
import com.krutkowski.cars.model.dto.AwsS3SaveImageModel;
import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.entity.File;
import com.krutkowski.cars.model.entity.Image;
import com.krutkowski.cars.model.mapper.CarMapper;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.repository.CarRepository;
import com.krutkowski.cars.repository.ImageRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final ImageRepo imageRepository;
    private final CarMapper carMapper;
    private final AmazonS3 amazonS3Client;
    private final AwsS3Config amazonS3Config;

    @PersistenceContext
    private EntityManager entityManager;

    public void saveCar(CarRequest carRequest) {
        Car car = Car.builder()
                .id(carRequest.getId())
                .brand(carRequest.getBrand())
                .model(carRequest.getModel())
                .title(carRequest.getTitle())
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

    @Transactional
    public void saveCarWithFiles(@Valid CarRequest carRequest, List<MultipartFile> files) {
        Car car = mapCarFromRequest(carRequest);
        carRepository.save(car);
        saveImagesForCar(files, car);
    }

    private Car mapCarFromRequest(@Valid CarRequest carRequest) {
        return Car.builder()
                .id(carRequest.getId())
                .brand(carRequest.getBrand())
                .model(carRequest.getModel())
                .title(carRequest.getTitle())
                .price(carRequest.getPrice())
                .year(carRequest.getYear())
                .mileage(carRequest.getMileage())
                .power(carRequest.getPower())
                .type(carRequest.getType())
                .location(carRequest.getLocation())
                .flag(carRequest.getFlag())
                .color(carRequest.getColor())
                .engine(carRequest.getEngine())
                .fuelType(carRequest.getFuelType())
                .build();
    }

    private void saveImagesForCar(List<MultipartFile> files, Car car) {
        for (MultipartFile file : files) {
            AwsS3SaveImageModel savedImageToAWS = saveImageToAWS(file);
            Image imageToSave = Image.builder()
                    .imageUrl(savedImageToAWS.awsUrl())
                    .name(savedImageToAWS.fileName())
                    .created(new Date(System.currentTimeMillis()))
                    .modified(new Date(System.currentTimeMillis()))
                    .car(car)
                    .build();

            imageRepository.save(imageToSave);
        }
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

    public Page<CarDTO> getFilteredCars(Map<String, String> filters, int pageNumber, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Car> cq = cb.createQuery(Car.class);
        Root<Car> car = cq.from(Car.class);

        List<Predicate> predicatesForQuery = new ArrayList<>();

        if (filters.containsKey("brand")) {
            predicatesForQuery.add(cb.equal(cb.lower(car.get("brand")), filters.get("brand").toLowerCase()));
        }
        if (filters.containsKey("model")) {
            predicatesForQuery.add(cb.equal(cb.lower(car.get("model")), filters.get("model").toLowerCase()));
        }
        if (filters.containsKey("title")) {
            predicatesForQuery.add(cb.like(cb.lower(car.get("title")), filters.get("title").toLowerCase()));
        }
        if (filters.containsKey("priceFrom")) {
            predicatesForQuery.add(cb.ge(car.get("price"), Integer.parseInt(filters.get("priceFrom"))));
        }
        if (filters.containsKey("priceTo")) {
            predicatesForQuery.add(cb.le(car.get("price"), Integer.parseInt(filters.get("priceTo"))));
        }
        if (filters.containsKey("yearFrom")) {
            predicatesForQuery.add(cb.ge(car.get("price"), Integer.parseInt(filters.get("yearFrom"))));
        }
        if (filters.containsKey("yearTo")) {
            predicatesForQuery.add(cb.le(car.get("year"), Integer.parseInt(filters.get("yearTo"))));
        }
        if (filters.containsKey("mileageFrom")) {
            predicatesForQuery.add(cb.ge(car.get("mileage"), Integer.parseInt(filters.get("mileageFrom"))));
        }
        if (filters.containsKey("mileageTo")) {
            predicatesForQuery.add(cb.le(car.get("mileage"), Integer.parseInt(filters.get("mileageTo"))));
        }
        if (filters.containsKey("powerFrom")) {
            predicatesForQuery.add(cb.ge(car.get("power"), Integer.parseInt(filters.get("powerFrom"))));
        }
        if (filters.containsKey("powerTo")) {
            predicatesForQuery.add(cb.le(car.get("power"), Integer.parseInt(filters.get("powerTo"))));
        }
        if (filters.containsKey("engineCapacityFrom")) {
            predicatesForQuery.add(cb.ge(car.get("engine"), Integer.parseInt(filters.get("engineCapacityFrom"))));
        }
        if (filters.containsKey("engineCapacityTo")) {
            predicatesForQuery.add(cb.le(car.get("engine"), Integer.parseInt(filters.get("engineCapacityTo"))));
        }
        if (filters.containsKey("bodyType")) {
            predicatesForQuery.add(cb.equal(cb.lower(car.get("type")), filters.get("bodyType").toLowerCase()));
        }
        if (filters.containsKey("originCountry")) {
            predicatesForQuery.add(cb.equal(cb.lower(car.get("location")), filters.get("originCountry").toLowerCase()));
        }
        if (filters.containsKey("color")) {
            predicatesForQuery.add(cb.equal(cb.lower(car.get("color")), filters.get("color").toLowerCase()));
        }

        cq.where(cb.and(predicatesForQuery.toArray(new Predicate[0])));

        TypedQuery<Car> query = entityManager.createQuery(cq);

        List<Car> carEntities = query.getResultList();

        int total = carEntities.size();
        int first = pageNumber * pageSize;

        List<CarDTO> carDTOs = carEntities.stream()
                .map(carMapper::toDto)
                .skip(first)
                .limit(first + pageSize)
                .toList();

        return new PageImpl<>(carDTOs, PageRequest.of(pageNumber, pageSize), total);
    }

}
