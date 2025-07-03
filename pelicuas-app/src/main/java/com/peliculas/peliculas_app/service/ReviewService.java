package com.peliculas.peliculas_app.service;


import com.peliculas.peliculas_app.model.Pelicula;
import com.peliculas.peliculas_app.model.Review;
import com.peliculas.peliculas_app.model.Usuario;
import com.peliculas.peliculas_app.repository.PeliculaRepository;
import com.peliculas.peliculas_app.repository.ReviewRepository;
import com.peliculas.peliculas_app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewsByPeliculaId(Long peliculaId) {
        return reviewRepository.findByPeliculaId(peliculaId);
    }

    public Review saveReview(Long peliculaId, Long usuarioId, Review review) {
        Pelicula pelicula = peliculaRepository.findById(peliculaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Película no encontrada con ID: " + peliculaId));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + usuarioId));

        review.setPelicula(pelicula);
        review.setUsuario(usuario);

        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}