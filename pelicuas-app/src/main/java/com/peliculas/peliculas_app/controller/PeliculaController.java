package com.peliculas.peliculas_app.controller;

import com.peliculas.peliculas_app.model.Pelicula;
import com.peliculas.peliculas_app.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<Pelicula>> getPeliculasPorGenero(@PathVariable String genero) {
        List<Pelicula> peliculas = peliculaService.getPeliculasPorGenero(genero);
        return new ResponseEntity<>(peliculas, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Pelicula>> getAllPeliculas() {
        List<Pelicula> peliculas = peliculaService.getAllPeliculas();
        return new ResponseEntity<>(peliculas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> getPeliculaById(@PathVariable Long id) {
        return peliculaService.getPeliculaById(id)
                .map(pelicula -> new ResponseEntity<>(pelicula, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Pelicula> createPelicula(@RequestBody Pelicula pelicula) {
        Pelicula newPelicula = peliculaService.savePelicula(pelicula);
        return new ResponseEntity<>(newPelicula, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pelicula> updatePelicula(@PathVariable Long id, @RequestBody Pelicula peliculaDetails) {
        return peliculaService.getPeliculaById(id)
                .map(existingPelicula -> {
                    existingPelicula.setTitulo(peliculaDetails.getTitulo());
                    existingPelicula.setDirector(peliculaDetails.getDirector());
                    existingPelicula.setAnoLanzamiento(peliculaDetails.getAnoLanzamiento());
                    existingPelicula.setGenero(peliculaDetails.getGenero());
                    existingPelicula.setSinopsis(peliculaDetails.getSinopsis());
                    existingPelicula.setUrlPoster(peliculaDetails.getUrlPoster());
                    Pelicula updatedPelicula = peliculaService.savePelicula(existingPelicula);
                    return new ResponseEntity<>(updatedPelicula, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePelicula(@PathVariable Long id) {
        peliculaService.deletePelicula(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}