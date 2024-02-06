package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.GameEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameDTO {
    private int pk_GameId;
    private int fk_PlayerId;
    private int die1;
    private int die2;
    private boolean victory;

    public GameDTO() {

    }
/*
    public GameDTO(GameEntity game) {
        this.pk_GameId = game.getPk_GameId();
        this.fk_PlayerId = game.getPlayerId();
        this.die1 = game.getDie1();
        this.die2 = game.getDie2();
        this.victory = die1 + die2 == 7;
    }

    public GameEntity toEntity() {
        GameEntity game = new GameEntity();
        game.setPk_GameId(pk_GameId);
        game.setPlayerId(fk_PlayerId);
        game.setDie1(die1);
        game.setDie2(die2);
        return game;
    }
*/
}
