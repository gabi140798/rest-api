package pl.kurs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "book_title", length = 500)
    private String title;
    private String category;
    private boolean available;

    public Book(String title, String category, boolean available) {
        this.title = title;
        this.category = category;
        this.available = available;
    }
}
