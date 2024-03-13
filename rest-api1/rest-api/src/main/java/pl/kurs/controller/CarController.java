package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.repository.CarRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/cars")
@Slf4j
@RequiredArgsConstructor
public class CarController {

    private final CarRepository carRepository;

    @PostConstruct
    public void init() {
        carRepository.saveAndFlush(new Car("Audi", "RS6", "Petrol"));
        carRepository.saveAndFlush(new Car("BMW", "E46", "Diesel"));
    }

    @GetMapping
    public ResponseEntity<List<Car>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(carRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody CreateCarCommand command) {
        Car car = new Car(command.getBrand(), command.getModel(), command.getFuelType());
        return ResponseEntity.status(HttpStatus.CREATED).body(carRepository.saveAndFlush(car));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> findCar(@PathVariable int id) {
        return ResponseEntity.ok(carRepository.findById(id).orElseThrow(CarNotFoundException::new));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteCar(@PathVariable int id) {
        carRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> editCar(@PathVariable int id, @RequestBody EditCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setBrand(command.getBrand());
        car.setModel(command.getModel());
        car.setFuelType(command.getFuelType());
        return ResponseEntity.status(HttpStatus.OK).body(carRepository.saveAndFlush(car));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Car> editCarPartially(@PathVariable int id, @RequestBody EditCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        Optional.ofNullable(command.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(command.getModel()).ifPresent(car::setModel);
        Optional.ofNullable(command.getFuelType()).ifPresent(car::setFuelType);
        return ResponseEntity.status(HttpStatus.OK).body(carRepository.saveAndFlush(car));
    }
}
