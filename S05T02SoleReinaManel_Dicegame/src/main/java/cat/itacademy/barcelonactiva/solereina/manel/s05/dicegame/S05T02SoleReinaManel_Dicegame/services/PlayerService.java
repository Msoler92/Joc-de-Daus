package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;

import java.util.List;

public interface PlayerService {
    PlayerDTO save(PlayerDTO player);
    PlayerDTO update(PlayerDTO player);
    PlayerDTO getById(int id);
    List<PlayerDTO> getAll();
    double getAverageRanking();
    PlayerDTO getLoser();
    PlayerDTO getWinner();

}
