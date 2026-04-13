package com.sergioroldan.gametracker.controller;


import com.sergioroldan.gametracker.dto.GameDTO;
import com.sergioroldan.gametracker.model.GameStatus;
import com.sergioroldan.gametracker.service.GameService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Registra los endpoints y convierte las respuestas en JSON automaticamente
@RestController
@RequestMapping("/games")
public class GameController {

private final GameService gameService;

public GameController(GameService gameService) {
    this.gameService = gameService;

}

@GetMapping

public List<GameDTO> getAllGames( @RequestParam(required = false) GameStatus status, @RequestParam(required = false) String platform) {

return gameService.getAllGames(status, platform);

}

@GetMapping("/{id}")
public GameDTO getGameById(@PathVariable Long id)  { //@PathVariable lee la URL

return  gameService.getGameByID(id);

}

@PostMapping
    public GameDTO createNewGame(@RequestBody @Valid GameDTO dto){ // @RequestBody lee del body en JSOn y lo convierte en un objeto Java.

    return gameService.createGame(dto);
}

@PutMapping("/{id}")
    public GameDTO updateGame(@PathVariable Long id ,@RequestBody @Valid GameDTO dto) {

    return gameService.updateGame(id, dto);
}

@DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {

    gameService.deleteGame(id);

}


}
