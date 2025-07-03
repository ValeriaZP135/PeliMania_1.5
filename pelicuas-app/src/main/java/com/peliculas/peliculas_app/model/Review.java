package com.peliculas.peliculas_app.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comentario;
    private int calificacion; // 1-5 estrellas, por ejemplo

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pelicula_id")
    @JsonIgnoreProperties("reviews")

    private Pelicula pelicula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties("reviews")
    private Usuario usuario; // El usuario que hizo la review
}
