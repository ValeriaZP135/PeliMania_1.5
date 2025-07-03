package com.peliculas.peliculas_app.service;

import com.peliculas.peliculas_app.model.Pelicula;
import com.peliculas.peliculas_app.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;

    public List<Pelicula> getAllPeliculas() {
        List<Pelicula> peliculas = peliculaRepository.findAll();
        peliculas.forEach(Pelicula::calcularCalificacionPromedio); // Calcula la calificación al recuperar
        return peliculas;
    }

    public Optional<Pelicula> getPeliculaById(Long id) {
        Optional<Pelicula> pelicula = peliculaRepository.findById(id);
        pelicula.ifPresent(Pelicula::calcularCalificacionPromedio);
        return pelicula;
    }

    public List<Pelicula> getPeliculasPorGenero(String genero) {
        return peliculaRepository.findByGeneroIgnoreCase(genero);
    }

    public Pelicula savePelicula(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }

    public void deletePelicula(Long id) {
        peliculaRepository.deleteById(id);
    }
}