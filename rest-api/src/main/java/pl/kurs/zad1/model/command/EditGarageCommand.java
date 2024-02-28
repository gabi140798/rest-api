package pl.kurs.zad1.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditGarageCommand {

    private String places,address;
    private boolean lpgAllowed;
}
