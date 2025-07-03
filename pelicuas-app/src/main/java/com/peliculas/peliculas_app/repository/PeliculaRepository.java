package com.peliculas.peliculas_app.repository;

import com.peliculas.peliculas_app.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    List<Pelicula> findByGeneroIgnoreCase(String genero);
    
}
