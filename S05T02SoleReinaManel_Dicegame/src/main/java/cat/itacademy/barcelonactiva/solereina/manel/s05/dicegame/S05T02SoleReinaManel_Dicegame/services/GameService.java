package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.GameDTO;

import java.util.List;

public interface GameService {
    GameDTO playGame(int playerID);
    List<GameDTO> getByPlayerId(int playerID);
    void deleteByPlayer(int playerID);

}
