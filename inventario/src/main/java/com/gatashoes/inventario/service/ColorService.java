package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Color;
import com.gatashoes.inventario.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    public List<Color> listarColores() {
        return colorRepository.findAll();
    }

    public Color guardarColor(Color color) {
        return colorRepository.save(color);
    }

    public Color obtenerColorPorId(Integer idColor) {
        return colorRepository.findById(idColor).orElse(null);
    }

    public Color obtenerColorPorIdOrThrow(Integer idColor) {
        return colorRepository.findById(idColor)
                .orElseThrow(() -> new ResourceNotFoundException("Color no encontrado con id " + idColor));
    }

    public Color actualizarColor(Color color) {
        return colorRepository.save(color);
    }

    public void eliminarColor(Integer idColor) {
        if (!colorRepository.existsById(idColor)) {
            throw new ResourceNotFoundException("Color no encontrado con id " + idColor);
        }
        colorRepository.deleteById(idColor);
    }
}
