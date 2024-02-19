package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.impl;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.exceptions.EntityNotFoundException;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories.GameRepository;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories.PlayerRepository;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.GameService;
import jakarta.transaction.Transactional;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public GameDTO playGame(int playerId) {
        PlayerEntity player = playerRepository.findById(playerId).orElseThrow(() -> new EntityNotFoundException("Id not found."));
        GameEntity game = new GameEntity(player);
        game.play();
        return entityToDTO(gameRepository.save(game));
    }

    @Override
    public List<GameDTO> getByPlayerId(int playerId) {
        playerRepository.findById(playerId).orElseThrow(() -> new EntityNotFoundException("Id not found."));
        return gameRepository.findByPlayerId(playerId).stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteByPlayer(int playerID) {
        gameRepository.deleteByPlayerId(playerID);
    }

    private GameDTO entityToDTO(GameEntity entity) {
        ModelMapper modelMapper = new ModelMapper();
        Converter<GameEntity, Boolean> victoryCondition = ctx -> ctx.getSource() == null ? null : ctx.getSource().getDie1() + ctx.getSource().getDie2() == 7;
        GameDTO dto = new GameDTO();
        TypeMap<GameEntity, GameDTO> propertyMapper = modelMapper.createTypeMap(GameEntity.class, GameDTO.class);

        propertyMapper.addMappings(mapper -> mapper.using(victoryCondition).map(game -> game, GameDTO::setVictory));
        propertyMapper.map(entity, dto);
        return dto;
    }
    private GameEntity dtoToEntity(GameDTO dto) {
        ModelMapper mapper = new ModelMapper();
        GameEntity entity = new GameEntity();

        mapper.map(dto, entity);
        return entity;
    }
}
