package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.controllers;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.services.GameService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GameRESTController {
    @Autowired
    GameService gameService;

    @Operation(summary = "Play a new game and save it")
    @PostMapping("/players/{id}/games")
    public ResponseEntity<GameDTO> playGame(@PathVariable("id") Integer id, HttpServletRequest request) {
        request.getHeader("username");
        return new ResponseEntity<>(gameService.playGame(id), HttpStatus.CREATED);
    }
    @Operation(summary = "Delete all games belonging to a given player")
    @DeleteMapping("/players/{id}/games")
    public ResponseEntity<GameDTO> deleteGames(@PathVariable("id") Integer id) {
        gameService.deleteByPlayer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Show all games for a given player")
    @GetMapping("/players/{id}/games")
    public ResponseEntity<List<GameDTO>> getGames(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(gameService.getByPlayerId(id), HttpStatus.OK);
    }

}
