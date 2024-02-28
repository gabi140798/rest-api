package pl.kurs.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditGarageCommand {

    private Integer places;
    private String address;
    private Boolean lpgAllowed;
}
