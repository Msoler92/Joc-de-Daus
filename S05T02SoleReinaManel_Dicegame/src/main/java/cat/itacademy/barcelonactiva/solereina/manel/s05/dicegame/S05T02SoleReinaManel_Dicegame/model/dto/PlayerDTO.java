package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
public class PlayerDTO {
    private int id;
    private String playerName;
    private LocalDate creationDate;
    private double victoryRate;
}