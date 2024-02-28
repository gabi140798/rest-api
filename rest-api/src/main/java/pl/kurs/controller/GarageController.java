package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

    @RestController
    @RequestMapping("/api/v1/garages")
    @Slf4j
    public class GarageController {

        private List<Garage> list = Collections.synchronizedList(new ArrayList<>());

        private AtomicInteger generator = new AtomicInteger(0);

        @PostConstruct
        public void init(){
            list.add(new Garage(generator.incrementAndGet(), 10, "address1", true));
            list.add(new Garage(generator.incrementAndGet(), 20, "address2", false));
        }

        @GetMapping
        public ResponseEntity<List<Garage>> findAll(){
            log.info("findAll");
            return ResponseEntity.ok(list);
        }

        @PostMapping
        public ResponseEntity<Garage> addGarage(@RequestBody CreateGarageCommand command){
            Garage garage = new Garage(generator.incrementAndGet(), command.getPlaces(), command.getAddress(), command.getLpgAllowed());
            list.add(garage);
            return ResponseEntity.status(HttpStatus.CREATED).body(garage);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Garage> findGarage(@PathVariable int id){
            return ResponseEntity.ok(list.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Garage> deleteGarage(@PathVariable int id){
            if(!list.removeIf(b -> b.getId() == id)){
                throw new GarageNotFoundException();
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @PutMapping("/{id}")
        public ResponseEntity<Garage> editGarage(@PathVariable int id, @RequestBody EditGarageCommand command){
            Garage garage = list.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new);
            garage.setAddress(command.getAddress());
            garage.setPlaces(command.getPlaces());
            garage.setLpgAllowed(command.getLpgAllowed());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @PatchMapping("/{id}")
        public ResponseEntity<Garage> editGaragePartially(@PathVariable int id, @RequestBody EditGarageCommand command){
            Garage garage = list.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new);
            Optional.ofNullable(command.getAddress()).ifPresent(garage::setAddress);
            Optional.ofNullable(command.getPlaces()).ifPresent(garage::setPlaces);
            Optional.ofNullable(command.getLpgAllowed()).ifPresent(garage::setLpgAllowed);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

