package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.controllers;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.utils.PlayerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
public class PlayerIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

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

    @DisplayName("PlayerControllerIntegrationTest - createPlayer inserts new player")
    @Test
    void createPlayer_should_create_player() throws Exception {
        PlayerDTO playerDTO = PlayerDTO.builder()
                .playerName("Player 4")
                .build();

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.playerName", is("Player 4")))
                .andExpect(jsonPath("$.victoryRate").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty());
    }
    @DisplayName("PlayerControllerIntegrationTest - createPlayer returns exception if player exists")
    @Test
    void createPlayer_should_throw_exception_if_name_exists() throws Exception{
        PlayerDTO playerDTO = PlayerDTO.builder()
                .playerName("Player 1")
                .build();
        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO))
                )
                .andDo(print())
                .andExpect(status().isConflict());
    }
    @DisplayName("PlayerControllerIntegrationTest - createPlayer returns exception if player exists")
    @RepeatedTest(2) //Make sure default name can be assigned multiple times
    void createPlayer_should_assign_default_name() throws Exception {
        PlayerDTO playerDTO = new PlayerDTO();

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.playerName", is(PlayerUtils.DEFAULT_NAME)))
                .andExpect(jsonPath("$.victoryRate").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty());
    }
    @DisplayName("PlayerControllerIntegrationTest - updatePlayer modifies existing player")
    @Test
    void updatePlayer_should_modify_player() throws Exception{
        PlayerDTO playerDTO = PlayerDTO.builder()
                .id(1)
                .playerName("UpdatePlayer 1")
                .build();

        mockMvc.perform(put("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.playerName", is("UpdatePlayer 1")))
                .andExpect(jsonPath("$.victoryRate").isNotEmpty())
                .andExpect(jsonPath("$").isNotEmpty());
    }
    @DisplayName("PlayerControllerIntegrationTest - updatePlayer returns exception if player not found")
    @Test
    void updatePlayer_should_throw_exception_if_player_not_found() throws Exception {
        PlayerDTO playerDTO = PlayerDTO.builder()
                .id(4)
                .playerName("UpdatePlayer 1")
                .build();

        mockMvc.perform(put("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @DisplayName("PlayerControllerIntegrationTest - getAllPlayers returns list of players")
    @Test
    void getAllPlayers_should_return_list_of_players() throws Exception {
        mockMvc.perform(get("/players")
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
    @DisplayName("PlayerControllerIntegrationTest - getAverageRanking returns average as double")
    @Test
    void getAverageRanking_should_return_double_value() throws Exception {
        mockMvc.perform(get("/players/ranking")
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(0.5d));
    }
    @DisplayName("PlayerControllerIntegrationTest - getLoser returns player with least score")
    @Test
    void getLoser_should_return_player_with_least_score() throws Exception {

        mockMvc.perform(get("/players/ranking/loser")
                        .contentType(MediaType.APPLICATION_JSON)
                                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.playerName", is("Player 3")))
                .andExpect(jsonPath("$.victoryRate").value(0d))
                .andExpect(jsonPath("$").isNotEmpty());
    }
    @DisplayName("PlayerControllerIntegrationTest - getWinner returns player with highest score")
    @Test
    void getWinner_should_return_player_with_highest_score() throws Exception {

        mockMvc.perform(get("/players/ranking/winner")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.playerName", is("Player 2")))
                .andExpect(jsonPath("$.victoryRate").value(1d))
                .andExpect(jsonPath("$").isNotEmpty());
    }
}
