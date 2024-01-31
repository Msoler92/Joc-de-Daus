package cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.controllers;

import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.solereina.manel.s05.dicegame.S05T02SoleReinaManel_Dicegame.model.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerRESTController {
    @Autowired
    PlayerService playerService;

    @PostMapping("/players")
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO dto) {
        return new ResponseEntity<>(playerService.save(dto), HttpStatus.CREATED);
    }

    @PutMapping("/players")
    public ResponseEntity<PlayerDTO> updatePlayer(@RequestBody PlayerDTO dto) {
        return new ResponseEntity<>(playerService.update(dto), HttpStatus.OK);
    }

    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return new ResponseEntity<>(playerService.getALl(), HttpStatus.OK);
    }

    @GetMapping("/players/ranking")
    public ResponseEntity<Double> getAverageRanking() {
        return new ResponseEntity<>(playerService.getAverageRanking(), HttpStatus.OK);
    }

    @GetMapping("/players/ranking/loser")
    public ResponseEntity<PlayerDTO> getLoser() {
        return new ResponseEntity<>(playerService.getLoser(), HttpStatus.OK);
    }

    @GetMapping("/players/ranking/winner")
    public ResponseEntity<PlayerDTO> getWinner() {
        return new ResponseEntity<>(playerService.getWinner(), HttpStatus.OK);
    }
}
