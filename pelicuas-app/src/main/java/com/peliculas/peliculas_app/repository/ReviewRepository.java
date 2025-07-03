package com.peliculas.peliculas_app.repository;



import com.peliculas.peliculas_app.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPeliculaId(Long peliculaId);
}