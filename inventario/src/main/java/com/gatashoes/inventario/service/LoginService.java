package com.gatashoes.inventario.service;

import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public Optional<Administrador> validarLogin(
            String correo,
            String contrasena
    ) {

        Optional<Administrador> admin =
                administradorRepository.findByCorreo(correo);

        if (admin.isPresent()
                && admin.get().getContrasena().equals(contrasena)) {

            return admin;
        }

        return Optional.empty();
    }
}