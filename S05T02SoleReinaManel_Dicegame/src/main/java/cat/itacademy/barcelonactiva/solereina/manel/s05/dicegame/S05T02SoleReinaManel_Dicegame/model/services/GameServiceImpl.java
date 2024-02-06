package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.services;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.exceptions.EntityNotFoundException;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.repositories.GameRepository;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.repositories.PlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService{
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public GameDTO playGame(int playerID) {
        PlayerEntity player = playerRepository.findById(playerID).orElseThrow(() -> new EntityNotFoundException("Id not found."));
        GameEntity game = new GameEntity(player);
        game.play();
        return entityToDTO(gameRepository.save(game));
    }

    @Override
    public List<GameDTO> getByPlayerId(int playerId) {
        return gameRepository.findByPlayerId(playerId).stream().map(this::entityToDTO).collect(Collectors.toList());
    }
    @Override
    public GameDTO save(GameDTO dto) {
        GameEntity entity = dtoToEntity(dto);
        return entityToDTO(gameRepository.save(entity));
    }
    @Override
    public void deleteByPlayer(int playerID) {
        gameRepository.deleteByPlayerId(playerID);
    }
    @Override
    public double getPlayerAverage(int playerID) {
        double average;
        List<GameEntity> games = gameRepository.findByPlayerId(playerID);

        if (games.isEmpty()) {
            average = 0;
        } else {
            average = games.stream().filter(GameEntity::validateVictory).count();
            average /= games.size();
        }
        return average;
    }
    //TODO fix
    private GameDTO entityToDTO(GameEntity entity) {
        ModelMapper mapper = new ModelMapper();
        GameDTO dto = new GameDTO();

        mapper.map(entity, dto);
        dto.setVictory(entity.getDie1() + entity.getDie2() == 7); //TODO temporary working solution. Use TypeMapper.
        return dto;
    }
    private GameEntity dtoToEntity(GameDTO dto) {
        ModelMapper mapper = new ModelMapper();
        GameEntity entity = new GameEntity();

        mapper.map(dto, entity);
        return entity;
    }
}
