package com.gatashoes.inventario.service;

import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.repository.AdministradorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final AdministradorRepository repository;

    public LoginService(AdministradorRepository repository) {
        this.repository = repository;
    }

    public boolean validarLogin(String correo, String contrasena) {

        Optional<Administrador> admin =
                repository.findByCorreoAndContrasena(correo, contrasena);

        return admin.isPresent();
    }
}