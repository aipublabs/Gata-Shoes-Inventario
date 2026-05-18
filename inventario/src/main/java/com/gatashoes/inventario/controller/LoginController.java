package com.gatashoes.inventario.controller;

import com.gatashoes.inventario.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/acceso")
    public String mostrarLogin() {
        return "Login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session
    ) {

        boolean valido = loginService.validarLogin(email, password);

        if (valido) {

            session.setAttribute("usuario", email);

            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Usuario o contraseña incorrectos");

        return "Login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {

        if (session.getAttribute("usuario") == null) {
            return "redirect:/acceso";
        }

        return "dashboard";
    }
}
