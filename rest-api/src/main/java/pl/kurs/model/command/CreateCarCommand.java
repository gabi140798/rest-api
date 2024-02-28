package pl.kurs.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCarCommand {

    private String brand,model,fuelType;
}
