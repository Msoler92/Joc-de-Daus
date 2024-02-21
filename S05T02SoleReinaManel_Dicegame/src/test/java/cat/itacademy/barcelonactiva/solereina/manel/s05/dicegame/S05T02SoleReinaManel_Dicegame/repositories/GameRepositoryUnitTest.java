package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.GameEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameRepositoryUnitTest {
    @Autowired
    GameRepository gameRepository;

    @BeforeAll
    static void reset(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "players");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "games");

        jdbcTemplate.execute("INSERT INTO players (id, creation_date, player_name) VALUES (1, '" + LocalDate.now() + "', 'Player 1');");

        jdbcTemplate.execute("INSERT INTO games (id, die_one, die_two, player_id) VALUES (1, 3, 4, 1);");
        jdbcTemplate.execute("INSERT INTO games (id, die_one, die_two, player_id) VALUES (2, 2, 2, 1);");
        
    }

    @DisplayName("GameRepositoryUnitTest - findByPlayerId returns list")
    @Test
    void findByPlayerId_should_return_list() {
        List<GameEntity> games = gameRepository.findByPlayerId(1);
        assertEquals(2, games.size());
    }

    @DisplayName("GameRepositoryUnitTest - deleteByPlayerId deletes all games from player")
    @Test
    void deleteByPlayerId_should_delete_all_games_from_player() {
        gameRepository.deleteByPlayerId(1);
        List<GameEntity> games = gameRepository.findByPlayerId(1);
        assertEquals(0, games.size());
    }

}
