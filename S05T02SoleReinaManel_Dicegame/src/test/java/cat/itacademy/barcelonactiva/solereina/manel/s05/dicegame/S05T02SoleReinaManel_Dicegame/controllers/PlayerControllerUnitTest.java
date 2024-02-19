package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.controllers;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.security.JwtFilter;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.JwtService;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.PlayerService;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.UserService;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.impl.JwtServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.*;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest(controllers = PlayerRESTController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PlayerControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlayerService playerService;
    @MockBean
    private JwtFilter jwtFilter;
    @Autowired
    private  ObjectMapper objectMapper;

    private PlayerDTO playerDTO1;
    private PlayerDTO playerReturnDTO1;

    @BeforeEach
    void setUp() {
        playerDTO1 = PlayerDTO.builder()
                .playerName("Player 1")
                .build();
        playerReturnDTO1 = PlayerDTO.builder()
                .id(1)
                .playerName("Player 1")
                .creationDate(LocalDate.of(1111, 11, 11))
                .victoryRate(0)
                .build();

    }
    @AfterEach
    void reset() {
        playerDTO1 = null;
        playerReturnDTO1 = null;
    }

    @DisplayName("PlayerControllerUnitTest - createPlayer inserts new player")
    @Test
    void createPlayer_should_add_new_player() throws Exception {
        when(playerService.save(any(PlayerDTO.class))).thenReturn(playerReturnDTO1);

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO1))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.playerName", is("Player 1")))
                .andExpect(jsonPath("$.victoryRate", is(0d)))
                .andExpect(jsonPath("$").isNotEmpty());
    }
    @DisplayName("PlayerControllerUnitTest - updatePlayer updates existing player")
    @Test
    void update_player_should_modify_existing_player() throws Exception {
        when(playerService.update(any(PlayerDTO.class))).thenReturn(playerReturnDTO1);

        mockMvc.perform(put("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.playerName", is("Player 1")))
                .andExpect(jsonPath("$.victoryRate", is(0d)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("PlayerControllerUnitTest - getAllPlayers returns all players with their average scores")
    @Test
    void getAllPlayers_should_return_player_list() throws Exception {
        PlayerDTO playerReturnDTO2 = PlayerDTO.builder()
                .id(2)
                .playerName("Player 2")
                .creationDate(LocalDate.of(1111, 11, 11))
                .victoryRate(0.5d)
                .build();
        when(playerService.getAll()).thenReturn(List.of(playerReturnDTO1, playerReturnDTO2));

        mockMvc.perform(get("/players"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$").isArray());
    }

    @DisplayName("PlayerControllerUnitTest - getAverageRanking returns global average score")
    @Test
    void getAverageRanking_should_return_double() throws Exception {
        when(playerService.getAverageRanking()).thenReturn(0.25d);
        mockMvc.perform(get("/players/ranking"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(0.25d));
    }

    @DisplayName("PlayerControllerUnitTest - getLoser returns player")
    @Test
    void getLoser_should_return_player() throws Exception {
        when(playerService.getLoser()).thenReturn(playerReturnDTO1);

        mockMvc.perform(get("/players/ranking/loser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.playerName", is("Player 1")))
                .andExpect(jsonPath("$.victoryRate", is(0d)))
                .andExpect(jsonPath("$").isNotEmpty());
    }
    @DisplayName("PlayerControllerUnitTest - getWinner returns player")
    @Test
    void getWinner_should_return_player() throws Exception {
        when(playerService.getLoser()).thenReturn(playerReturnDTO1);

        mockMvc.perform(get("/players/ranking/loser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.playerName", is("Player 1")))
                .andExpect(jsonPath("$.victoryRate", is(0d)))
                .andExpect(jsonPath("$").isNotEmpty());
    }
}
