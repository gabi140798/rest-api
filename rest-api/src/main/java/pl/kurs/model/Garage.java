package pl.kurs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Garage {

    private int id;
    private int places;
    private String address;
    private boolean lpgAllowed;
}
