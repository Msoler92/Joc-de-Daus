package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.repositories;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.PlayerEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlayerRepositoryUnitTest {
    @Autowired
    private PlayerRepository playerRepository;

    @BeforeAll
    static void reset(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "players");

        jdbcTemplate.execute("INSERT INTO players (id, creation_date, player_name) VALUES (1, '" + LocalDate.now() + "', 'Player 1');");
    }
    @DisplayName("PlayerRepositoryUnitTest - findByPlayerName returns optional")
    @Test
    void findByPlayerName_should_return_optional() {
        Optional<PlayerEntity> player = playerRepository.findByPlayerName("Player 1");
        assertTrue(player.isPresent());

        player = playerRepository.findByPlayerName("Player 7");
        assertTrue(player.isEmpty());
    }
}
