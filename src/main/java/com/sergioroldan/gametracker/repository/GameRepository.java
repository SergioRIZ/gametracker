package com.sergioroldan.gametracker.repository;


import com.sergioroldan.gametracker.model.Game;
import com.sergioroldan.gametracker.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Marca una clase de acceso de datos
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    // Spring Data JPA crea automáticamente la implementación
    // basándose en el nombre del método. Esto se llama "query methods".
    List<Game> findByStatus(GameStatus status);
    List<Game> findByPlatform(String platform);
    Optional<Game> findByTitle(String title);
    List<Game> findByStatusAndPlatform(GameStatus status, String platform);

}