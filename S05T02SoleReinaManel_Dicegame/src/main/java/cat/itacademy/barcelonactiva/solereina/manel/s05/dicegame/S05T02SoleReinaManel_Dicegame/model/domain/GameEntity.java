package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "games")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pk_GameId;
    @Column(name = "fk_playerid")
    private int fk_PlayerId;
    @Column(name = "die_one")
    private int die1;
    @Column(name = "die_two")
    private int die2;


    public GameEntity() {
    }

    public GameEntity(int fk_PlayerId) {
        this.fk_PlayerId = fk_PlayerId;
    }

    public boolean play() {
        boolean victory;

        die1 = (int) (Math.random()*5 + 1);
        die2 = (int) (Math.random()*5 + 1);

        victory = die1 + die2 == 7;

        return victory;
    }
}
