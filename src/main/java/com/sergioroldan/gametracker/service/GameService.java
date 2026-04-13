package com.sergioroldan.gametracker.service;
import com.sergioroldan.gametracker.dto.GameDTO;
import com.sergioroldan.gametracker.exception.GameAlreadyExistsException;
import com.sergioroldan.gametracker.exception.GameNotFoundException;
import com.sergioroldan.gametracker.exception.InvalidGameDataException;
import com.sergioroldan.gametracker.model.Game;
import com.sergioroldan.gametracker.model.GameStatus;
import com.sergioroldan.gametracker.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;


// Service te dice que esta clase es un componente de logica de negocio
@Service
public class GameService {

    private final GameRepository gameRepository;

    // Inyeccion de dependencias por constructor
    public GameService(GameRepository gameRepository) {

        this.gameRepository = gameRepository;
    }

    private GameDTO toDTO(Game game) {
        return new GameDTO(
                game.getId(),
                game.getTitle(),
                game.getPlatform(),
                game.getGenre(),
                game.getReleaseYear(),
                game.getStatus()
        );
    }

    private Game toEntity(GameDTO dto) {
        Game game = new Game();
        game.setTitle(dto.getTitle());
        game.setPlatform(dto.getPlatform());
        game.setGenre(dto.getGenre());
        game.setReleaseYear(dto.getReleaseYear());
        game.setStatus(dto.getStatus());

        return  game;
    }

    // Regla de negocio: el año no puede ser futuro salvo que el estado sea PENDING
    private void validateReleaseYear(GameDTO dto) {
        int currentYear = Year.now().getValue();
        if (dto.getReleaseYear() > currentYear && dto.getStatus() != GameStatus.PENDING) {
            throw new InvalidGameDataException(
                "El año de salida no puede ser futuro salvo si el estado es PENDING"
            );
        }
    }

    public List<GameDTO> getAllGames(GameStatus status, String platform){

        List<Game> juegos;
        if (status == null && platform == null) {
            juegos = gameRepository.findAll();
        } else if(status != null && platform == null){
            juegos = gameRepository.findByStatus(status);
        } else if (status == null) {
            juegos = gameRepository.findByPlatform(platform);
        } else {
            juegos = gameRepository.findByStatusAndPlatform(status, platform);
        }

        return juegos.stream()
                .map(this::toDTO)
                .toList();
    }

    public GameDTO getGameByID(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));

        return toDTO(game);
    }

    public GameDTO createGame(GameDTO dto) {

        if(gameRepository.findByTitle(dto.getTitle()).isPresent()){
            throw new GameAlreadyExistsException(dto.getTitle());
        }
        validateReleaseYear(dto);
        Game game = gameRepository.save(toEntity(dto));
        return toDTO(game);
    }

    public GameDTO updateGame(Long id, GameDTO dto) {

        Optional<Game> existing = gameRepository.findByTitle(dto.getTitle());
        if(existing.isPresent() && !existing.get().getId().equals(id)){
            throw new GameAlreadyExistsException(dto.getTitle());
        }
        validateReleaseYear(dto);
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
                game.setTitle(dto.getTitle());
                game.setPlatform(dto.getPlatform());
                game.setGenre(dto.getGenre());
                game.setReleaseYear(dto.getReleaseYear());
                game.setStatus(dto.getStatus());

                gameRepository.save(game);

                return toDTO(game);

    }

    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));

        gameRepository.delete(game);
    }

}
