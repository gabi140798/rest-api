package pl.kurs.model.command;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class CreateBookCommand {

    private String title;
    private String category;
}
