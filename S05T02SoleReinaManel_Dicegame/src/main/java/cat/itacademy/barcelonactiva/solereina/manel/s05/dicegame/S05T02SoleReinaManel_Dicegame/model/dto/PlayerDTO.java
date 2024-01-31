package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.PlayerEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PlayerDTO {
    private int pk_PlayerId;
    private String playerName;
    private Date creationDate;

    public PlayerDTO() {

    }

    public PlayerDTO(PlayerEntity player) {
        this.pk_PlayerId = player.getPk_PlayerId();
        this.playerName = player.getPlayerName();
        this.creationDate = player.getCreationDate();
    }

    public PlayerEntity toEntity() {
        PlayerEntity player = new PlayerEntity();
        player.setPk_PlayerId(pk_PlayerId);
        player.setPlayerName(playerName);
        player.setCreationDate(creationDate);
        return player;
    }
}