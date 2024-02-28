package pl.kurs.zad2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Car {

    private int id;
    private String brand,model,fuelType;
}
