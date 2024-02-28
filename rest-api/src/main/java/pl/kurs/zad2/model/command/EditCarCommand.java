package pl.kurs.zad2.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCarCommand {

    private String brand,model,fuelType;
}
