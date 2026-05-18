package com.gatashoes.inventario.controller;

import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    // =========================
    // MOSTRAR LOGIN
    // =========================
    @GetMapping("/login")
    public String mostrarLogin() {
        return "Login";
    }

    // =========================
    // PROCESAR LOGIN
    // =========================
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            Model model
    ) {

        Optional<Administrador> admin =
                loginService.validarLogin(correo, contrasena);

        if (admin.isPresent()) {
            return "redirect:/resumen";
        }

        model.addAttribute("error", true);

        return "Login";
    }
}