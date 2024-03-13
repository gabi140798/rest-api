package pl.kurs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity

public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "places", length = 500)
    private int places;
    private String address;
    private boolean lpgAllowed;

    public Garage(int places, String address, boolean lpgAllowed) {
        this.places = places;
        this.address = address;
        this.lpgAllowed = lpgAllowed;
    }
}
