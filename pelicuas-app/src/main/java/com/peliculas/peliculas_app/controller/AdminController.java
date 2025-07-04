package com.peliculas.peliculas_app.controller;

import com.peliculas.peliculas_app.model.Pelicula;
import com.peliculas.peliculas_app.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private PeliculaService peliculaService;
    
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Pelicula> peliculas = peliculaService.getAllPeliculas();
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("totalPeliculas", peliculas.size());
        
        // Estadísticas adicionales
        long totalGeneros = peliculas.stream()
                .map(Pelicula::getGenero)
                .distinct()
                .count();
        
        model.addAttribute("totalGeneros", totalGeneros);
        return "admin-dashboard"; // ← Este archivo debe existir
    }
}