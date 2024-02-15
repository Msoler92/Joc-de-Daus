package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.exceptions.EntityNotFoundException;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories.GameRepository;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories.PlayerRepository;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.impl.GameServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceUnitTest {
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private GameServiceImpl gameService;

    private GameDTO gameDTO;
    private GameEntity gameEntity;

    private PlayerEntity playerEntity;
    @BeforeEach
    void setUp() {
        playerEntity = PlayerEntity.builder()
                .id(1)
                .playerName("Player 1")
                .creationDate(Date.valueOf(LocalDate.of(1111, 11, 11))).build();
        gameDTO = GameDTO.builder().id(1).playerId(1).die1(3).die2(4).victory(true).build();
        gameEntity = GameEntity.builder().id(1).die1(3).die2(4).player(playerEntity).build();
    }
    @AfterEach
    void reset() {
        gameDTO = null;
        gameEntity = null;
        playerEntity = null;
    }

    @DisplayName("GameServiceUnitTest - playGame inserts new game")
    @Test
    void playGame_should_insert_new_game() {
        when(gameRepository.save(any(GameEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        when(playerRepository.findById(1)).thenReturn(Optional.of(playerEntity));

        gameDTO = gameService.playGame(1);

        ArgumentCaptor<GameEntity> gameEntityArgumentCaptor = ArgumentCaptor.forClass(GameEntity.class);
        verify(gameRepository, times(1)).save(gameEntityArgumentCaptor.capture());

        gameEntity = gameEntityArgumentCaptor.getValue();
        assertEquals(gameEntity.getDie1(), gameDTO.getDie1());
        assertEquals(gameEntity.getDie2(), gameDTO.getDie2());
    }
    @DisplayName("GameServiceUnitTest - playGame throws EntityNotFoundException if player does not exist")
    @Test
    void playGame_should_throw_EntityNotFoundException() {
        when(playerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gameService.playGame(1));
    }

    @DisplayName("GameServiceUnitTest - GetByPlayerId returns list of GameDTO")
    @Test
    void getByPlayerId_should_return_list_of_gameDTO() {
        List<GameDTO> games;
        GameEntity gameEntity2 = GameEntity.builder().id(2).die1(5).die2(4).player(playerEntity).build();

        when(playerRepository.findById(1)).thenReturn(Optional.of(playerEntity));
        when(gameRepository.findByPlayerId(1)).thenReturn(List.of(gameEntity, gameEntity2));

        games = gameService.getByPlayerId(1);
        assertEquals(2, games.size());
        verify(playerRepository).findById(1);
        verify(gameRepository).findByPlayerId(1);
    }

    @DisplayName("GameServiceUnitTest - getByPlayerId throws exception if player cannot be found")
    @Test
    void getByPlayerId_should_throw_exception_if_player_does_not_exist() {
        when(playerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gameService.getByPlayerId(1));
    }
    @DisplayName("GameServiceUnitTest - deleteByPlayer deletes games belonging to that player")
    @Test
    void deleteByPlayer_should_delete_games() {
        gameService.deleteByPlayer(1);
        verify(gameRepository).deleteByPlayerId(1);
    }


}
