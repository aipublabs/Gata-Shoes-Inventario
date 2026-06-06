package com.gatashoes.inventario.api.security;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AdministradorService administradorService;

    public Administrador autenticar(String correo, String contrasena) {
        return administradorService.obtenerAdminPorCorreo(correo)
                .filter(admin -> admin.getContrasena() != null && admin.getContrasena().equals(contrasena))
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));
    }
}
