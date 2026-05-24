package com.gatashoes.inventario.controller;

import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest; // Importante para persistir la sesión
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "Login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            Model model,
            HttpServletRequest request // Inyectamos el request para guardar la sesión de seguridad
    ) {
        // Ejecuta tu consulta original a MySQL sin alteraciones
        Optional<Administrador> admin = loginService.validarLogin(correo, contrasena);

        if (admin.isPresent()) {
            // =========================================================================
            // LÍNEAS NUEVAS: REGISTRO MANUAL DE AUTENTICACIÓN EN EL FRAMEWORK
            // =========================================================================
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    admin.get(), null, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            // Guardamos esta autorización en la sesión HTTP para que no se pierda en el redireccionamiento
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
            // =========================================================================

            return "redirect:/resumen";
        }

        // Mantiene intacto tu flujo de error original que ya pintaba tu caja de alerta en el HTML
        model.addAttribute("error", true);
        return "Login";
    }
}