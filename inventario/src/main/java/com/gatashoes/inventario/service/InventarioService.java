package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Inventario;
import com.gatashoes.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }

    public Inventario guardarInventario(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public Inventario obtenerInventarioPorId(Integer idInventario) {
        return inventarioRepository.findById(idInventario).orElse(null);
    }

    public Inventario obtenerInventarioPorIdOrThrow(Integer idInventario) {
        return inventarioRepository.findById(idInventario)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id " + idInventario));
    }

    public Inventario actualizarInventario(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public void eliminarInventario(Integer idInventario) {
        if (!inventarioRepository.existsById(idInventario)) {
            throw new ResourceNotFoundException("Inventario no encontrado con id " + idInventario);
        }
        inventarioRepository.deleteById(idInventario);
    }

    public List<Inventario> listarAlertas() {
        return inventarioRepository.findAll().stream()
                .filter(inventario -> inventario.getStock() != null && inventario.getStock() <= 3)
                .toList();
    }

    public List<Inventario> listarNovedades() {
        return inventarioRepository.findTop5ByOrderByIdInventarioDesc();
    }

    public List<Inventario> listarTopStock() {
        return inventarioRepository.findTop3ByOrderByStockDesc();
    }
}
