package pl.kurs.zad1.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.zad1.exceptions.GarageNotFoundException;
import pl.kurs.zad1.model.Garage;
import pl.kurs.zad1.model.command.CreateGarageCommand;
import pl.kurs.zad1.model.command.EditGarageCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

    @RestController
    @RequestMapping("/api/v1/garage")
    @Slf4j
    public class GarageController {

        private List<Garage> list = Collections.synchronizedList(new ArrayList<>());

        private AtomicInteger generator = new AtomicInteger(0);

        @PostConstruct
        public void init(){
            list.add(new Garage(generator.incrementAndGet(), "xyz", "address1", true));
            list.add(new Garage(generator.incrementAndGet(), "xyz2", "address2", false));
        }

        @GetMapping
        public ResponseEntity<List<Garage>> findAll(){
            log.info("findAll");
            return ResponseEntity.ok(list);
        }

        @PostMapping
        public ResponseEntity<Garage> addGarage(@RequestBody CreateGarageCommand command){
            Garage garage = new Garage(generator.incrementAndGet(), command.getPlaces(), command.getAddress(), true);
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
            garage.setLpgAllowed(command.isLpgAllowed());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @PatchMapping("/{id}")
        public ResponseEntity<Garage> editGaragePartially(@PathVariable int id, @RequestBody EditGarageCommand command){
            Garage garage = list.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new);
            Optional.ofNullable(command.getAddress()).ifPresent(garage::setAddress);
            Optional.ofNullable(command.getPlaces()).ifPresent(garage::setPlaces);
            //Optional.ofNullable(command.getLpgAllowed()).ifPresent(garage::setLpgAllowed);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

