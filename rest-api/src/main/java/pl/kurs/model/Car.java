package pl.kurs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Car {

    private int id;
    private String brand;
    private String model;
    private String fuelType;
}
