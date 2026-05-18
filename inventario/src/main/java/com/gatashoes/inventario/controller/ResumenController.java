package com.gatashoes.inventario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResumenController {

    @GetMapping("/resumen")
    public String mostrarResumen() {
        return "Resumen"; // Spring Boot buscará automáticamente /WEB-INF/jsp/Resumen.jsp
    }
}