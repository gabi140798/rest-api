package pl.kurs.zad1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Garage {

    private int id;
    private String places,address;
    private boolean lpgAllowed;
}
