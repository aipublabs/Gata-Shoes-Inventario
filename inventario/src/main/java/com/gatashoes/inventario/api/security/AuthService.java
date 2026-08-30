package com.gatashoes.inventario.api.security;

import com.gatashoes.inventario.api.exception.CredencialesInvalidasException;
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

    /**
     * Autentica un administrador validando su correo y contraseña.
     *
     * Si el correo no existe o la contraseña no coincide,
     * lanza CredencialesInvalidasException sin revelar cuál de los dos datos falló.
     *
     * Para contraseñas sin cifrar (migración), las cifra automáticamente
     * con BCrypt al verificar éxito.
     *
     * @param correo Correo del administrador
     * @param contrasena Contraseña sin cifrar
     * @return Administrador autenticado
     * @throws CredencialesInvalidasException si correo no existe o contraseña no coincide
     */
    public Administrador autenticar(String correo, String contrasena) {
        Administrador admin = administradorService.obtenerAdminPorCorreo(correo)
                .orElseThrow(() -> new CredencialesInvalidasException("Credenciales inválidas"));

        String storedPassword = admin.getContrasena();
        if (storedPassword == null) {
            throw new CredencialesInvalidasException("Credenciales inválidas");
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
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        return admin;
    }
}
