package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class GameIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void reset(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "players");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "games");

        jdbcTemplate.execute("INSERT INTO players (id, creation_date, player_name) VALUES (1, '" + LocalDate.now() + "', 'Player 1');");
        jdbcTemplate.execute("INSERT INTO players (id, creation_date, player_name) VALUES (2, '" + LocalDate.now() + "', 'Player 2');");
        jdbcTemplate.execute("INSERT INTO players (id, creation_date, player_name) VALUES (3, '" + LocalDate.now() + "', 'Player 3');");

        jdbcTemplate.execute("INSERT INTO games (id, die_one, die_two, player_id) VALUES (1, 3, 4, 1);");
        jdbcTemplate.execute("INSERT INTO games (id, die_one, die_two, player_id) VALUES (2, 2, 2, 1);");
        jdbcTemplate.execute("INSERT INTO games (id, die_one, die_two, player_id) VALUES (3, 2, 5, 2);");
        jdbcTemplate.execute("INSERT INTO games (id, die_one, die_two, player_id) VALUES (4, 1, 6, 2);");
        jdbcTemplate.execute("INSERT INTO games (id, die_one, die_two, player_id) VALUES (5, 1, 2, 3);");
        jdbcTemplate.execute("INSERT INTO games (id, die_one, die_two, player_id) VALUES (6, 5, 5, 3);");
    }

    @DisplayName("GameIntegrationTest - playGame inserts new game")
    @Test
    void playGame_should_insert_new_game() throws Exception{
        mockMvc.perform(post("/players/1/games")
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.playerId", is(1)))
                .andExpect(jsonPath("$.die1").isNotEmpty())
                .andExpect(jsonPath("$.die2").isNotEmpty())
                .andExpect(jsonPath("$.victory").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("GameIntegrationTest - playGame throws EntityNotFoundException if player does not exist")
    @Test
    void playGame_should_throw_EntityNotFoundException() throws Exception{
        mockMvc.perform(post("/players/7/games")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("GameIntegrationTest - deleteGames removes games")
    @Test
    void deleteGames_should_remove_games() throws Exception {
        mockMvc.perform(delete("/players/1/games")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }
    @DisplayName("GameIntegrationTest - deleteGames throws EntityNotFoundException if player does not exist")
    @Test
    void deleteGames_should_throw_EntityNotFoundException() throws Exception{
        mockMvc.perform(delete("/players/7/games")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @DisplayName("GameIntegrationTest - getGames returns list of games")
    @Test
    void getGames_should_return_list_of_games() throws Exception{
        mockMvc.perform(get("/players/1/games")
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
    @DisplayName("GameIntegrationTest - getGames throws EntityNotFoundException if player does not exist")
    @Test
    void getGames_should_throw_EntityNotFoundException() throws Exception{
        mockMvc.perform(get("/players/7/games")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
