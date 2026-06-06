package com.gatashoes.inventario.api.security;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Administrador autenticar(String correo, String contrasena) {
        Administrador admin = administradorService.obtenerAdminPorCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

        String storedPassword = admin.getContrasena();
        if (storedPassword == null) {
            throw new ResourceNotFoundException("Credenciales inválidas");
        }

        boolean matches;
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            matches = passwordEncoder.matches(contrasena, storedPassword);
        } else {
            matches = storedPassword.equals(contrasena);
            if (matches) {
                admin.setContrasena(passwordEncoder.encode(contrasena));
                administradorService.actualizarAdministrador(admin);
            }
        }

        if (!matches) {
            throw new ResourceNotFoundException("Credenciales inválidas");
        }

        return admin;
    }
}
