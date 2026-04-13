package com.sergioroldan.gametracker.dto;

import com.sergioroldan.gametracker.model.GameStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Nos genera todos los metodos getters, setters, toString(), equals() y hasCode().
@NoArgsConstructor // Crea un constructor vacio public Game
@AllArgsConstructor //Crea un constructor con todos los campos public Game

public class GameDTO {

    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    private String title;

    @NotBlank(message = "La plataforma a indicar es obligatoria")
    private String platform;

    private String genre;

    @Min(value= 1958, message ="El año de salida no puede ser anterior a 1958")
    private Integer releaseYear;

    @NotNull(message = "El estado es obligatorio")
    private GameStatus status;

}
