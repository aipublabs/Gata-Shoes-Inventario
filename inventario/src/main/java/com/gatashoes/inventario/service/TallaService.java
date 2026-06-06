package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Talla;
import com.gatashoes.inventario.repository.TallaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TallaService {

    @Autowired
    private TallaRepository tallaRepository;

    public List<Talla> listarTallas() {
        return tallaRepository.findAll();
    }

    public Talla guardarTalla(Talla talla) {
        return tallaRepository.save(talla);
    }

    public Talla obtenerTallaPorId(Integer idTalla) {
        return tallaRepository.findById(idTalla).orElse(null);
    }

    public Talla obtenerTallaPorIdOrThrow(Integer idTalla) {
        return tallaRepository.findById(idTalla)
                .orElseThrow(() -> new ResourceNotFoundException("Talla no encontrada con id " + idTalla));
    }

    public Talla actualizarTalla(Talla talla) {
        return tallaRepository.save(talla);
    }

    public void eliminarTalla(Integer idTalla) {
        if (!tallaRepository.existsById(idTalla)) {
            throw new ResourceNotFoundException("Talla no encontrada con id " + idTalla);
        }
        tallaRepository.deleteById(idTalla);
    }
}
