package com.sergioroldan.gametracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// Esta clase representa una tabla en una BBDD. Hibernate creara automaticamente la tabla
@Entity
// Le dice a JPA que esta entidad se mapea en la tabla games
@Table(name="games")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Game  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hace obligatorio ingresar datos en esta tabla, si nos, dara error
    @Column (nullable = false)
    private String title;

    @Column (nullable = false)
    private String platform;

    private String genre;

    // Mapea el campo Java releaseYear a la columna SQL release_year
    @Column(name = "release_year")
    private Integer releaseYear;

    // Le dice a JPA que guarda el enum como texto en la BBDD. Esta es una buena práctica.
    // ya que con String se guarda como "playing" o "completed".
    // Si lo haces con enum al reordenar los datos quedan mal.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

}
