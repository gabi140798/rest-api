package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.EditCarCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/cars")
@Slf4j
public class CarController {

    private List<Car> cars = Collections.synchronizedList(new ArrayList<>());

    private AtomicInteger generator = new AtomicInteger(0);

    @PostConstruct
    public void init(){
        cars.add(new Car(generator.incrementAndGet(), "Audi", "RS6", "Petrol"));
        cars.add(new Car(generator.incrementAndGet(), "BMW", "E46", "Diesel"));
    }

    @GetMapping
    public ResponseEntity<List<Car>> findAll(){
        log.info("findAll");
        return ResponseEntity.ok(cars);
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody CreateCarCommand command){
        Car car = new Car(generator.incrementAndGet(), command.getBrand(), command.getModel(), command.getFuelType());
        cars.add(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(car);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> findCar(@PathVariable int id){
        return ResponseEntity.ok(cars.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(CarNotFoundException::new));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteCar(@PathVariable int id){
        if(!cars.removeIf(b -> b.getId() == id)){
            throw new CarNotFoundException();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> editCar(@PathVariable int id, @RequestBody EditCarCommand command){
        Car car = cars.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(CarNotFoundException::new);
        car.setBrand(command.getBrand());
        car.setModel(command.getModel());
        car.setFuelType(command.getFuelType());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Car> editCarPartially(@PathVariable int id, @RequestBody EditCarCommand command){
        Car car = cars.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(CarNotFoundException::new);
        Optional.ofNullable(command.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(command.getModel()).ifPresent(car::setModel);
        Optional.ofNullable(command.getFuelType()).ifPresent(car::setFuelType);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
