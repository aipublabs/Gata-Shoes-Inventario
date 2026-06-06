package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public List<Administrador> listarAdministradores() {
        return administradorRepository.findAll();
    }

    public Administrador guardarAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public Administrador obtenerAdminPorId(Integer idAdministrador) {
        return administradorRepository.findById(idAdministrador).orElse(null);
    }

    public Administrador obtenerAdminPorIdOrThrow(Integer idAdministrador) {
        return administradorRepository.findById(idAdministrador)
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado con id " + idAdministrador));
    }

    public Optional<Administrador> obtenerAdminPorCorreo(String correo) {
        return administradorRepository.findByCorreo(correo);
    }

    public Administrador actualizarAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public void eliminarAdministrador(Integer idAdministrador) {
        if (!administradorRepository.existsById(idAdministrador)) {
            throw new ResourceNotFoundException("Administrador no encontrado con id " + idAdministrador);
        }
        administradorRepository.deleteById(idAdministrador);
    }
}
