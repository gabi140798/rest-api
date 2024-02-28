package pl.kurs.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGarageCommand {

    private int places;
    private String address;
    private Boolean lpgAllowed;
}
