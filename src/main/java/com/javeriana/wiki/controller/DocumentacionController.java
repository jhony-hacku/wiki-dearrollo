package com.javeriana.wiki.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.javeriana.wiki.repository.EstudianteRepository;

@Controller
public class DocumentacionController {

    // Documentos de la entrega final. Cada uno vive en static/docs como .pdf (para verlo aqui) y .docx (editable).
    // Clase con getters (no record) para que Thymeleaf lea las propiedades como ${d.titulo}
    public static class Documento {
        private final String codigo;
        private final String titulo;
        private final String descripcion;
        private final String archivo;

        public Documento(String codigo, String titulo, String descripcion, String archivo) {
            this.codigo = codigo;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.archivo = archivo;
        }

        public String getCodigo() { return codigo; }
        public String getTitulo() { return titulo; }
        public String getDescripcion() { return descripcion; }
        public String getArchivo() { return archivo; }
    }

    private static final List<Documento> DOCUMENTOS = List.of(
            new Documento("01", "Resumen ejecutivo", "Objetivo, solución, resultados consolidados de las pruebas y estado de entrega.", "01_Resumen_Ejecutivo"),
            new Documento("02", "Arquitectura C4", "Contexto, contenedores, componentes y modelo de dominio, con decisiones de arquitectura.", "02_Arquitectura_C4"),
            new Documento("03", "Despliegue", "Ambientes, pipeline CI/CD, configuración, migraciones, operación y rollback.", "03_Despliegue"),
            new Documento("04", "Pruebas de carga", "Escenarios de JMeter, umbrales y resultados contra staging.", "04_Pruebas_Carga"),
            new Documento("05", "Pruebas funcionales", "Casos de frontend y backend por historia de usuario y defectos encontrados.", "05_Pruebas_Funcionales"),
            new Documento("06", "Pruebas unitarias y SonarQube", "Pruebas unitarias, cobertura y resultados del análisis de SonarCloud.", "06_Unitarias_SonarQube"),
            new Documento("07", "Pruebas automatizadas", "Suite de Selenium, ejecución en CI y registro de ejecuciones.", "07_Pruebas_Automatizadas"),
            new Documento("08", "Roles y seguridad", "Roles, matriz de permisos, autenticación, autorización y pruebas de seguridad.", "08_Roles_y_Seguridad"));

    private final EstudianteRepository estudianteRepository;

    public DocumentacionController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @GetMapping({"/documentacion", "/documentacion/"})
    public String mostrarDocumentacion(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("documentos", DOCUMENTOS);
        return "documentacion";
    }
}
