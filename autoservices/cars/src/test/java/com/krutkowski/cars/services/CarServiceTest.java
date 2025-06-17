package com.krutkowski.cars.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.krutkowski.cars.config.AwsS3Config;
import com.krutkowski.cars.model.dto.CarDTO;
import com.krutkowski.cars.model.entity.Car;
import com.krutkowski.cars.model.entity.Image;
import com.krutkowski.cars.model.mapper.CarMapper;
import com.krutkowski.cars.model.request.CarRequest;
import com.krutkowski.cars.repository.CarRepository;
import com.krutkowski.cars.repository.ImageRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    private final CarRepository carRepository = mock(CarRepository.class);
    private final ImageRepo imageRepository = mock(ImageRepo.class);
    private final CarMapper carMapper = mock(CarMapper.class);
    private final AmazonS3 amazonS3Client = mock(AmazonS3.class);
    private final AwsS3Config amazonS3Config = mock(AwsS3Config.class);
    private final EntityManager entityManager = mock(EntityManager.class);
    private final CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
    private final CriteriaQuery<Car> criteriaQuery = mock(CriteriaQuery.class);
    private final Root<Car> carRoot = mock(Root.class);
    private final TypedQuery<Car> typedQuery = mock(TypedQuery.class);


    CarService carService = new CarService(carRepository, imageRepository, carMapper, amazonS3Client, amazonS3Config);

    CarRequest carRequest;

    Car car;


    @BeforeEach
    void setUp() {
        carRequest = CarRequest.builder()
                .id(1L)
                .brand("BMW")
                .model("X5")
                .title("SUV")
                .price(new BigDecimal("123456"))
                .year(2022)
                .mileage(80000)
                .power(240)
                .type("SUV")
                .location("Warszawa")
                .flag("PL")
                .color("Czarny")
                .engine("3.0 V6")
                .build();

        car = Car.builder()
                .id(1L)
                .brand("BMW")
                .model("X5")
                .title("Title")
                .price(new BigDecimal("50000"))
                .year(2020)
                .mileage(10000)
                .power(200)
                .type("SUV")
                .location("Warsaw")
                .flag("PL")
                .color("Black")
                .engine("3000")
                .fuelType("Diesel")
                .created(new Date())
                .modified(new Date())
                .images(new ArrayList<>())
                .build();
        ReflectionTestUtils.setField(carService, "entityManager", entityManager);
    }

    @Test
    void saveCar() {

        carService.saveCar(carRequest);

        ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
        verify(carRepository).save(captor.capture());

        Car saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getBrand()).isEqualTo("BMW");
        assertThat(saved.getModel()).isEqualTo("X5");
        assertThat(saved.getTitle()).isEqualTo("SUV");
        assertThat(saved.getPrice()).isEqualByComparingTo("123456");
        assertThat(saved.getYear()).isEqualTo(2022);
        assertThat(saved.getMileage()).isEqualTo(80000);
        assertThat(saved.getPower()).isEqualTo(240);
        assertThat(saved.getType()).isEqualTo("SUV");
        assertThat(saved.getLocation()).isEqualTo("Warszawa");
        assertThat(saved.getFlag()).isEqualTo("PL");
        assertThat(saved.getColor()).isEqualTo("Czarny");
        assertThat(saved.getEngine()).isEqualTo("3.0 V6");
    }

    @Test
    void getCarById() {
        long id = 1L;
        when(carRepository.findById(id)).thenReturn(Optional.ofNullable(car));
        when(carMapper.toDto(car)).thenReturn(new CarDTO(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getTitle(),
                null,
                car.getPrice(),
                car.getYear(),
                car.getMileage(),
                car.getPower(),
                car.getType(),
                car.getLocation(),
                car.getFlag(),
                car.getColor(),
                car.getEngine(),
                car.getFuelType(),
                new ArrayList<>()

        ));
        //then
        CarDTO carDTO = carService.getCarById(id);

        assertNotNull(carDTO);
        assertEquals("BMW", carDTO.brand());
        assertEquals("X5", carDTO.model());

    }

    @Test
    void shouldThrowExceptionWhenCarNotFound() {
        // given
        Long id = 42L;
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        // when + then
        assertThrows(NoSuchElementException.class, () -> carService.getCarById(id));
    }

    @Test
    void saveCarWithFiles() throws IOException {

        Car mappedCar = Car.builder().id(1L).build();

        when(carMapper.mapCarFromRequest(carRequest)).thenReturn(mappedCar);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(file.getSize()).thenReturn(100L);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("mock data".getBytes()));

        when(amazonS3Config.getBucketName()).thenReturn("mock-bucket");

        when(amazonS3Client.putObject(any(PutObjectRequest.class))).thenReturn(new PutObjectResult());

        when(imageRepository.findAllByCarAndIsMainImageTrue(mappedCar)).thenReturn(List.of());

        //test method
        carService.saveCarWithFiles(carRequest, List.of(file));

        //then
        verify(carRepository).save(mappedCar);
        verify(imageRepository).findAllByCarAndIsMainImageTrue(mappedCar);
        verify(imageRepository).save(any(Image.class));
        verify(amazonS3Client).putObject(any(PutObjectRequest.class));
    }


    @Test
    void shouldReturnFilteredCars() {
        // given
        Map<String, String> filters = Map.of("brand", "BMW");
        int page = 0;
        int size = 2;

        Car car1 = new Car(1L, "BMW", "X5", "SUV", new BigDecimal("30000"), 2019, 50000, 250, "SUV", "Germany", "", "black", "3000","Gasoline", new Date(), new Date(), new ArrayList<>());
        Car car2 = new Car(3L, "AUDI", "320i", "Sedan", new BigDecimal("25000"), 2018, 60000, 200, "Sedan", "Germany", "", "blue", "1600","Diesel", new Date(), new Date(), new ArrayList<>());
        List<Car> mockResult = List.of(car1, car2);

        // when
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Car.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Car.class)).thenReturn(carRoot);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(mockResult);
        CarDTO car1DTO = new CarDTO(
                car1.getId(),
                car1.getBrand(),
                car1.getModel(),
                car1.getTitle(),
                null,
                car1.getPrice(),
                car1.getYear(),
                car1.getMileage(),
                car1.getPower(),
                car1.getType(),
                car1.getLocation(),
                car1.getFlag(),
                car1.getColor(),
                car1.getEngine(),
                car1.getFuelType(),
                new ArrayList<>()
        );
        CarDTO car2DTO = new CarDTO(
                car2.getId(),
                car2.getBrand(),
                car2.getModel(),
                car2.getTitle(),
                null,
                car2.getPrice(),
                car2.getYear(),
                car2.getMileage(),
                car2.getPower(),
                car2.getType(),
                car2.getLocation(),
                car2.getFlag(),
                car2.getColor(),
                car2.getEngine(),
                car2.getFuelType(),
                new ArrayList<>()
        );
        when(carMapper.toDto(car1)).thenReturn(car1DTO);
        when(carMapper.toDto(car2)).thenReturn(car2DTO);


        // then
        Page<CarDTO> result = carService.getFilteredCars(filters, page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("BMW", result.getContent().get(0).brand());
        assertEquals("AUDI", result.getContent().get(1).brand());
    }
}