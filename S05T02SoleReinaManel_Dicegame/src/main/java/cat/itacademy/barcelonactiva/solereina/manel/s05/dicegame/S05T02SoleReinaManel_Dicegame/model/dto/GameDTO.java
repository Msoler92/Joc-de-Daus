package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private int id;
    private int playerId;
    private int die1;
    private int die2;
    private boolean victory;
}
