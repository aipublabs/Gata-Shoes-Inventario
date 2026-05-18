package com.gatashoes.inventario.repository;

import com.gatashoes.inventario.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository
        extends JpaRepository<Administrador, Integer> {

    Optional<Administrador> findByCorreo(String correo);
}