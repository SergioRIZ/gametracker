package com.sergioroldan.gametracker.exception;

public class GameNotFoundException extends RuntimeException{
    public GameNotFoundException(Long id) {
        super("No se ha encontrado ningun juego con ese id " +id);
    }
}
