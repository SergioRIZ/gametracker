package com.sergioroldan.gametracker.exception;

public class GameAlreadyExistsException extends RuntimeException{
    public GameAlreadyExistsException(String title) {super("No se puede duplicar un juego: " + title);}
}
