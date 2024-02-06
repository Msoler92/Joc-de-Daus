package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PlayerDTO {
    private int pk_PlayerId;
    private String playerName;
    private Date creationDate;
    private double victoryRate;

    public PlayerDTO() {

    }
}