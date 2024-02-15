package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.exceptions.EntityAlreadyExistsException;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.exceptions.EntityNotFoundException;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.utils.PlayerUtils;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories.GameRepository;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories.PlayerRepository;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.impl.PlayerServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceUnitTest {
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private PlayerServiceImpl playerService;

    private PlayerDTO playerDTO;
    private PlayerEntity playerEntity;
    private PlayerEntity playerEntity2;

    @BeforeEach
    void setUp() {
        playerDTO = PlayerDTO.builder()
                .id(1)
                .playerName("Player 1")
                .creationDate(LocalDate.of(1111, 11, 11)).build();
        playerEntity = PlayerEntity.builder()
                .id(1)
                .playerName("Player 1")
                .creationDate(Date.valueOf(LocalDate.of(1111, 11, 11))).build();
        playerEntity2 = PlayerEntity.builder()
                .id(2)
                .playerName("Player 2")
                .creationDate(Date.valueOf(LocalDate.of(1111, 11, 11))).build();
    }
    @AfterEach
    void reset() {
        playerDTO = null;
        playerEntity = null;
        playerEntity2 = null;
    }
    @DisplayName("PlayerServiceUnitTest - Save inserts new player")
    @Test
    void save_should_insert_new_player() { //TODO Review
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(playerEntity);

        playerService.save(playerDTO);

        ArgumentCaptor<PlayerEntity> playerEntityArgumentCaptor = ArgumentCaptor.forClass(PlayerEntity.class);
        verify(playerRepository, times(1)).save(playerEntityArgumentCaptor.capture());

        playerEntity = playerEntityArgumentCaptor.getValue();
        assertEquals(playerEntity.getPlayerName(), "Player 1");
    }

    //TODO save should assign new date

    @DisplayName("PlayerServiceUnitTest - Save assigns default name")
    @Test
    void save_should_assign_default_name() {
        ArgumentCaptor<PlayerEntity> playerEntityArgumentCaptor = ArgumentCaptor.forClass(PlayerEntity.class);

        playerDTO.setPlayerName(null);
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(playerEntity);

        playerService.save(playerDTO);
        verify(playerRepository, times(1)).save(playerEntityArgumentCaptor.capture());
        assertEquals(playerEntityArgumentCaptor.getValue().getPlayerName(), PlayerUtils.DEFAULT_NAME);

    }
    @DisplayName("PlayerServiceUnitTest - Save throws exception if player name already exists")
    @Test
    void save_should_throw_exception_if_name_already_exists() {

        when(playerRepository.findByPlayerName("Player 1")).thenReturn(Optional.of(playerEntity));

        assertThrows(EntityAlreadyExistsException.class, () -> playerService.save(playerDTO));
    }
    @DisplayName("PlayerServiceUnitTest - Save accepts multiple players with default name")
    @Test
    void save_should_accept_multiple_players_with_default_name() {
        PlayerDTO playerDTO2 = PlayerDTO.builder().id(2).playerName(PlayerUtils.DEFAULT_NAME).build();
        ArgumentCaptor<PlayerEntity> playerEntityArgumentCaptor = ArgumentCaptor.forClass(PlayerEntity.class);
        List<PlayerEntity> players;

        playerDTO.setPlayerName(null);
        playerEntity.setPlayerName(null);
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(playerEntity);

        playerService.save(playerDTO);
        playerService.save(playerDTO2);

        verify(playerRepository, times(2)).save(playerEntityArgumentCaptor.capture());

        players = playerEntityArgumentCaptor.getAllValues();

        assertEquals(players.get(0).getPlayerName(), players.get(1).getPlayerName());
        assertEquals(players.get(1).getPlayerName(), PlayerUtils.DEFAULT_NAME);
        assertNotEquals(players.get(0).getId(), players.get(1).getId());
    }
    @DisplayName("PlayerServiceUnitTest - Update modifies existing player")
    @Test
    void update_should_insert_modified_player() {
        ArgumentCaptor<PlayerEntity> playerEntityArgumentCaptor = ArgumentCaptor.forClass(PlayerEntity.class);

        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(playerEntity);
        when(playerRepository.findById(1)).thenReturn(Optional.of(playerEntity));

        playerDTO.setPlayerName("Modified name");
        playerService.update(playerDTO);
        verify(playerRepository, times(1)).save(playerEntityArgumentCaptor.capture());

        playerEntity = playerEntityArgumentCaptor.getValue();
        assertEquals(playerEntity.getPlayerName(), "Modified name");
    }
    @DisplayName("PlayerServiceUnitTest - Update throws exception if player cannot be found")
    @Test
    void update_should_throw_exception_if_player_does_not_exist() {
        when(playerRepository.findById(1)).thenReturn(Optional.empty());

        playerDTO.setPlayerName("Modified name");
        assertThrows(EntityNotFoundException.class, () -> playerService.update(playerDTO));
    }
    @DisplayName("PlayerServiceUnitTest - GetById returns playerDTO")
    @Test
    void getById_should_return_playerDTO() {
        PlayerDTO playerDTO2;

        when(playerRepository.findById(1)).thenReturn(Optional.of(playerEntity));

        playerDTO2 = playerService.getById(1);
        assertEquals(playerDTO.getId(), playerDTO2.getId());
        assertEquals(playerDTO.getPlayerName(), playerDTO2.getPlayerName());
        verify(playerRepository).findById(1);
    }

    @DisplayName("PlayerServiceUnitTest - GetById throws exception if player cannot be found")
    @Test
    void getById_should_throw_exception_if_player_does_not_exist() {
        when(playerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> playerService.update(playerDTO));
    }
    @DisplayName("PlayerServiceUnitTest - getAll returns list of PlayerDTO")
    @Test
    void getAll_should_return_list_of_playerDTO() {
        List<PlayerDTO> players;

        when(playerRepository.findAll()).thenReturn(List.of(playerEntity, playerEntity2));

        players = playerService.getAll();

        assertEquals(2, players.size());
        verify(playerRepository).findAll();
    }
    @DisplayName("PlayerServiceUnitTest - getAverageRanking returns double")
    @Test
    void getAverageRanking_should_return_double() {
        GameEntity game1 = GameEntity.builder().die1(3).die2(4).build();
        GameEntity game2 = GameEntity.builder().die1(2).die2(4).build();

        when(playerRepository.findAll()).thenReturn(List.of(playerEntity, playerEntity2));
        when(gameRepository.findByPlayerId(anyInt())).thenReturn(List.of(game1, game2));

        double avg = playerService.getAverageRanking();

        assertEquals(0.5d, avg);
    }
    @DisplayName("PlayerServiceUnitTest - getAverageRanking returns zero if there are no players")
    @Test
    void getAverageRanking_should_return_zero_if_no_players() {
        when(playerRepository.findAll()).thenReturn(new ArrayList<>());

        double avg = playerService.getAverageRanking();

        assertEquals(0d, avg);
        verifyNoInteractions(gameRepository);
    }
    @DisplayName("PlayerServiceUnitTest - getAverageRanking returns zero if there are no games")
    @Test
    void getAverageRanking_should_return_zero_if_no_games() {
        when(playerRepository.findAll()).thenReturn(List.of(playerEntity, playerEntity2));
        when(gameRepository.findByPlayerId(anyInt())).thenReturn(new ArrayList<>());

        double avg = playerService.getAverageRanking();

        assertEquals(0d, avg);
    }
    @DisplayName("PlayerServiceUnitTest - getLoser returns PlayerDTO")
    @Test
    void getLoser_should_return_PlayerDTO() {
        PlayerDTO loser;
        GameEntity game1 = GameEntity.builder().die1(3).die2(4).build();
        GameEntity game2 = GameEntity.builder().die1(2).die2(4).build();

        when(playerRepository.findAll()).thenReturn(List.of(playerEntity, playerEntity2));
        when(gameRepository.findByPlayerId(1)).thenReturn(List.of(game1, game2));
        when(gameRepository.findByPlayerId(2)).thenReturn(List.of(game1));

        loser = playerService.getLoser();

        assertEquals("Player 1", loser.getPlayerName());
    }

    @DisplayName("PlayerServiceUnitTest - getWinner returns PlayerDTO")
    @Test
    void getWinner_should_return_PlayerDTO() {
        PlayerDTO winner;

        GameEntity game1 = GameEntity.builder().die1(3).die2(4).build();
        GameEntity game2 = GameEntity.builder().die1(2).die2(4).build();

        when(playerRepository.findAll()).thenReturn(List.of(playerEntity, playerEntity2));
        when(gameRepository.findByPlayerId(1)).thenReturn(List.of(game1, game2));
        when(gameRepository.findByPlayerId(2)).thenReturn(List.of(game1));

        winner = playerService.getWinner();

        assertEquals("Player 2", winner.getPlayerName());
    }
}
