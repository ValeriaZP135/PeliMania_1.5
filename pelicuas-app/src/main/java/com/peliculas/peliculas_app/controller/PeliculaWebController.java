package com.peliculas.peliculas_app.controller;

import org.springframework.core.io.ClassPathResource;
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
import java.util.Optional;

@Controller
@RequestMapping("/peliculas")
public class PeliculaWebController {

    private final PeliculaService peliculaService;

    public PeliculaWebController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    // Ruta para usuarios normales (catálogo)
    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model) {
        List<Pelicula> peliculas = peliculaService.getAllPeliculas();
        model.addAttribute("peliculas", peliculas);
        return "index"; // Vista para usuarios
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
            try {
                String nombreOriginal = imagen.getOriginalFilename();
                String nombreUnico = System.currentTimeMillis() + "_" + nombreOriginal;
                
                // Usar directorio uploads (más confiable)
                Path directorioImagenes = Paths.get("uploads", "images");
                Files.createDirectories(directorioImagenes);
                
                Path rutaDestino = directorioImagenes.resolve(nombreUnico);
                Files.write(rutaDestino, imagen.getBytes());
                
                // URL que coincida con WebConfig
                pelicula.setUrlPoster("http://localhost:8080/uploads/images/" + nombreUnico);
                
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error al guardar imagen: " + e.getMessage());
            }
        }

        peliculaService.savePelicula(pelicula);
        return "redirect:/admin/dashboard";
    }

    // NUEVOS MÉTODOS PARA ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String confirmarEliminacion(@PathVariable Long id, Model model) {
        Optional<Pelicula> pelicula = peliculaService.getPeliculaById(id);
        if (pelicula.isPresent()) {
            model.addAttribute("pelicula", pelicula.get());
            return "confirmar-eliminacion";
        }
        return "redirect:/admin/dashboard?error=notfound";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPelicula(@PathVariable Long id) {
        try {
            peliculaService.deletePelicula(id);
            return "redirect:/admin/dashboard?success=deleted";
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=delete";
        }
    }

    @GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model model) {
        Optional<Pelicula> pelicula = peliculaService.getPeliculaById(id);
        if (pelicula.isPresent()) {
            model.addAttribute("pelicula", pelicula.get());
            return "detalle-pelicula";
        }
        return "redirect:/peliculas/catalogo?error=notfound";
    }

    // MÉTODOS PARA EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Optional<Pelicula> pelicula = peliculaService.getPeliculaById(id);
        if (pelicula.isPresent()) {
            model.addAttribute("pelicula", pelicula.get());
            return "nueva-pelicula"; // Reutilizar el mismo formulario
        }
        return "redirect:/admin/dashboard?error=notfound";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarPelicula(@PathVariable Long id, 
                                   @ModelAttribute Pelicula peliculaActualizada,
                                   @RequestParam("imagen") MultipartFile imagen) throws IOException {
        Optional<Pelicula> peliculaExistente = peliculaService.getPeliculaById(id);
        
        if (peliculaExistente.isPresent()) {
            Pelicula pelicula = peliculaExistente.get();
            
            // Actualizar campos
            pelicula.setTitulo(peliculaActualizada.getTitulo());
            pelicula.setDirector(peliculaActualizada.getDirector());
            pelicula.setGenero(peliculaActualizada.getGenero());
            pelicula.setAnoLanzamiento(peliculaActualizada.getAnoLanzamiento());
            pelicula.setSinopsis(peliculaActualizada.getSinopsis());
            
            // Solo actualizar imagen si se subió una nueva
            if (!imagen.isEmpty()) {
                try {
                    String nombreOriginal = imagen.getOriginalFilename();
                    String nombreUnico = System.currentTimeMillis() + "_" + nombreOriginal;
                    
                    Path directorioImagenes = Paths.get("uploads", "images");
                    Files.createDirectories(directorioImagenes);
                    
                    Path rutaDestino = directorioImagenes.resolve(nombreUnico);
                    Files.write(rutaDestino, imagen.getBytes());
                    
                    pelicula.setUrlPoster("http://localhost:8080/uploads/images/" + nombreUnico);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error al guardar imagen: " + e.getMessage());
                }
            }
            
            peliculaService.savePelicula(pelicula);
            return "redirect:/admin/dashboard?success=updated";
        }
        
        return "redirect:/admin/dashboard?error=notfound";
    }
}
