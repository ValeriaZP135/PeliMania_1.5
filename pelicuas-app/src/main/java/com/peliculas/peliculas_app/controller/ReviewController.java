package com.peliculas.peliculas_app.controller;

import com.peliculas.peliculas_app.model.Review;
import com.peliculas.peliculas_app.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id)
                .map(review -> new ResponseEntity<>(review, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/pelicula/{peliculaId}")
    public ResponseEntity<List<Review>> getReviewsByPeliculaId(@PathVariable Long peliculaId) {
        List<Review> reviews = reviewService.getReviewsByPeliculaId(peliculaId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/pelicula/{peliculaId}/usuario/{usuarioId}")
    public ResponseEntity<Review> createReview(@PathVariable Long peliculaId, @PathVariable Long usuarioId, @RequestBody Review review) {
        Review newReview = reviewService.saveReview(peliculaId, usuarioId, review);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review reviewDetails) {
        return reviewService.getReviewById(id)
                .map(existingReview -> {
                    existingReview.setComentario(reviewDetails.getComentario());
                    existingReview.setCalificacion(reviewDetails.getCalificacion());
                    // No permitir cambiar película o usuario directamente desde aquí para evitar inconsistencias
                    Review updatedReview = reviewService.saveReview(
                            existingReview.getPelicula().getId(),
                            existingReview.getUsuario().getId(),
                            existingReview
                    );
                    return new ResponseEntity<>(updatedReview, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}