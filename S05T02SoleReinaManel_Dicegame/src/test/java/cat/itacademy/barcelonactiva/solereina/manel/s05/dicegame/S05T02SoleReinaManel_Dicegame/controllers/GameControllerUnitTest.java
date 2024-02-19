package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.controllers;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.security.JwtFilter;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.GameService;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.PlayerService;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.UserService;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.impl.JwtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = GameRESTController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class GameControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GameService gameService;
    @MockBean
    private JwtFilter jwtFilter;
    @Autowired
    private ObjectMapper objectMapper;
    private GameDTO gameDTO;
    @BeforeEach
    void setUp() {
        gameDTO = GameDTO.builder()
                .id(1)
                .playerId(1)
                .die1(3)
                .die2(4)
                .victory(true).build();
    }
    @AfterEach
    void reset() {
        gameDTO = null;
    }

    @DisplayName("GameControllerUnitTest - playGame inserts new game")
    @Test
    void createPlayer_should_add_new_player() throws Exception {
        when(gameService.playGame(anyInt())).thenReturn(gameDTO);

        mockMvc.perform(post("/players/1/games")
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.playerId", is(1)))
                .andExpect(jsonPath("$.die1", is(3)))
                .andExpect(jsonPath("$.die2", is(4)))
                .andExpect(jsonPath("$.victory", is(true)))
                .andExpect(jsonPath("$").isNotEmpty());
    }
    @DisplayName("GameControllerUnitTest - deleteGames removes games")
    @Test
    void deleteGames_should_remove_games() throws Exception {
        mockMvc.perform(delete("/players/1/games"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
    @DisplayName("GameControllerUnitTest - getGames returns list of games")
    @Test
    void getGames_should_return_game_list() throws Exception {
        GameDTO gameDTO2 = GameDTO.builder()
                .id(2)
                .playerId(1)
                .die1(3)
                .die2(3)
                .victory(false).build();
        when(gameService.getByPlayerId(anyInt())).thenReturn(List.of(gameDTO, gameDTO2));

        mockMvc.perform(get("/players/1/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$").isArray());
    }

}
