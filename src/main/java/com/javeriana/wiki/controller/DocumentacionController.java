package com.javeriana.wiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.javeriana.wiki.repository.EstudianteRepository;

@Controller
public class DocumentacionController {

    private final EstudianteRepository estudianteRepository;

    public DocumentacionController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @GetMapping({"/documentacion", "/documentacion/"})
    public String mostrarDocumentacion(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        return "documentacion";
    }
}
