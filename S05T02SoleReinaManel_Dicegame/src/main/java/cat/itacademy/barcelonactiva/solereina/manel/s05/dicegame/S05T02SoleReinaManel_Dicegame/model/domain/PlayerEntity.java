package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.utils.PlayerUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "players")
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pk_PlayerId;
    @Column(name = "playerName")
    private String playerName;
    @Column(name = "creationDate")
    private Date creationDate;

    public PlayerEntity() {
        this.playerName = PlayerUtils.DEFAULT_NAME;
        this.creationDate = new Date();
    }
    public PlayerEntity(String playerName) {
        this.playerName = playerName;
        this.creationDate = new Date();
    }


}
