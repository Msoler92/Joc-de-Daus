package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.impl;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.exceptions.EmptyPlayerListException;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.exceptions.EntityAlreadyExistsException;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.exceptions.EntityNotFoundException;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.utils.PlayerUtils;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories.GameRepository;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories.PlayerRepository;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.PlayerService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;

    @Override
    public PlayerDTO save(PlayerDTO player) {
        String playerName = player.getPlayerName();
        if (playerName == null || playerName.equals(PlayerUtils.DEFAULT_NAME)) {
            player.setPlayerName(PlayerUtils.DEFAULT_NAME);
        }
        else if (playerRepository.findByPlayerName(playerName).isPresent()) {
            throw new EntityAlreadyExistsException("Player name already in use");
        }
        player.setCreationDate(LocalDate.now());
        return entityToDTO(
                playerRepository.save(
                        dtoToEntity(player)));
    }

    @Override
    public PlayerDTO update(PlayerDTO player) {
        getById(player.getId());
        return entityToDTO(
                playerRepository.save(
                        dtoToEntity(player)));
    }

    @Override
    public PlayerDTO getById(int id) {
        return entityToDTO(playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found.")));
    }

    @Override
    public List<PlayerDTO> getAll() {
        return playerRepository.findAll().stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public double getAverageRanking() {
        double average;
        List<PlayerDTO> playerDTOList = getAll();
        if (playerDTOList.isEmpty()) {
            average = 0;
        } else {
            average = playerDTOList.stream().mapToDouble(PlayerDTO::getVictoryRate).sum()/playerDTOList.size();
        }
        return average;
    }

    @Override
    public PlayerDTO getLoser() {
        List<PlayerDTO> players = getAll();
        return players.stream().min(Comparator.comparingDouble(PlayerDTO::getVictoryRate)).orElseThrow(() -> new EmptyPlayerListException("No players registered yet."));
    }

    //TODO Take into account ties for winner and loser?
    @Override
    public PlayerDTO getWinner() {
        List<PlayerDTO> players = getAll();
        return players.stream().max(Comparator.comparingDouble(PlayerDTO::getVictoryRate)).orElseThrow(() -> new EmptyPlayerListException("No players registered yet."));
    }

    private double getPlayerAverage(int playerId) { //TODO Reduce to single call?
        double average;
        List<GameEntity> games = gameRepository.findByPlayerId(playerId);

        if (games.isEmpty()) {
            average = 0;
        } else {
            average = games.stream().filter(GameEntity::validateVictory).count();
            average /= games.size();
        }
        return average;
    }
    private PlayerDTO entityToDTO(PlayerEntity entity) {
        ModelMapper mapper = new ModelMapper();
        //TODO fix
        //TypeMap<PlayerEntity, PlayerDTO> propertyMapper = mapper.createTypeMap(PlayerEntity.class, PlayerDTO.class);
        //propertyMapper.addMapping(player -> getPlayerAverage(player.getId()), PlayerDTO::setVictoryRate);
        //propertyMapper.addMapping(player -> player.getCreationDate().toLocalDate(), PlayerDTO::setCreationDate);
        PlayerDTO dto = new PlayerDTO();

        mapper.map(entity, dto);
        dto.setCreationDate(entity.getCreationDate().toLocalDate());
        dto.setVictoryRate(getPlayerAverage(entity.getId())); //TODO remove when above is fixed
        return dto;
    }

    private PlayerEntity dtoToEntity(PlayerDTO dto) {
        ModelMapper mapper = new ModelMapper();
        PlayerEntity entity = new PlayerEntity();

        mapper.map(dto, entity);
        entity.setCreationDate(Date.valueOf(dto.getCreationDate()));
        return entity;
    }
}
