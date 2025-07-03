package com.peliculas.peliculas_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.peliculas.peliculas_app.model.*;
import com.peliculas.peliculas_app.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@Controller
@RequestMapping("/peliculas")
public class PeliculaWebController {

    private final PeliculaService peliculaService;

    public ResponseEntity<List<Pelicula>> getPeliculasPorGenero(@PathVariable String genero) {
        List<Pelicula> peliculas = peliculaService.getPeliculasPorGenero(genero);
        return new ResponseEntity<>(peliculas, HttpStatus.OK);
    }

    public PeliculaWebController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "nueva-pelicula";
    }

    @PostMapping("/guardar")
    public String guardarPelicula(@ModelAttribute Pelicula pelicula,
                                  @RequestParam("imagen") MultipartFile imagen) throws IOException {

        if (!imagen.isEmpty()) {
            String nombreArchivo = imagen.getOriginalFilename();
            Path rutaDestino = Paths.get("src/main/resources/static/images/" + nombreArchivo);
            Files.write(rutaDestino, imagen.getBytes());
            pelicula.setUrlPoster("http://localhost:8080/images/" + nombreArchivo);
        }

        peliculaService.savePelicula(pelicula);
        return "redirect:/peliculas/nueva?success";
    }
}

