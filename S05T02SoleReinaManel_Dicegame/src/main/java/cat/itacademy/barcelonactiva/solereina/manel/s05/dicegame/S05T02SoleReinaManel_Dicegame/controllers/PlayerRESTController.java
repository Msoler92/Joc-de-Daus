package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.controllers;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerRESTController {
    @Autowired
    PlayerService playerService;
    @Operation(summary = "Create a new player")
    @PostMapping("/players")
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO dto) {
        return new ResponseEntity<>(playerService.save(dto), HttpStatus.CREATED);
    }
    @Operation(summary = "Overwrite an existing player")
    @PutMapping("/players")
    public ResponseEntity<PlayerDTO> updatePlayer(@RequestBody PlayerDTO dto) {
        return new ResponseEntity<>(playerService.update(dto), HttpStatus.OK);
    }
    @Operation(summary = "List all existing players with their average scores")
    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return new ResponseEntity<>(playerService.getAll(), HttpStatus.OK);
    }
    @Operation(summary = "Show the average ranking between all players")
    @GetMapping("/players/ranking")
    public ResponseEntity<Double> getAverageRanking() {
        return new ResponseEntity<>(playerService.getAverageRanking(), HttpStatus.OK);
    }
    @Operation(summary = "Show a player with the least score") //TODO fix description once Service is fixed
    @GetMapping("/players/ranking/loser")
    public ResponseEntity<PlayerDTO> getLoser() {
        return new ResponseEntity<>(playerService.getLoser(), HttpStatus.OK);
    }
    @Operation(summary = "Show a player with the highest score") //TODO fix description once Service is fixed
    @GetMapping("/players/ranking/winner")
    public ResponseEntity<PlayerDTO> getWinner() {
        return new ResponseEntity<>(playerService.getWinner(), HttpStatus.OK);
    }
}
