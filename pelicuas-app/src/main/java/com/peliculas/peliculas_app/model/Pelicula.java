package com.peliculas.peliculas_app.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String director;
    private int anoLanzamiento;
    private String genero;
    private String sinopsis;
    private String urlPoster; // URL a la imagen del póster

    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    // Campo calculado para la calificación promedio (no se mapea a la DB)
    @Transient
    private Double calificacionPromedio;

    // Método para calcular la calificación promedio
    public void calcularCalificacionPromedio() {
        if (this.reviews != null && !this.reviews.isEmpty()) {
            this.calificacionPromedio = this.reviews.stream()
                    .mapToDouble(Review::getCalificacion)
                    .average()
                    .orElse(0.0);
        } else {
            this.calificacionPromedio = 0.0;
        }
    }
}
