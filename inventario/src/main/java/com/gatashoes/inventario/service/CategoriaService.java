package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Categoria;
import com.gatashoes.inventario.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Obtener todas las categorías
     */
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    /**
     * Guardar nueva categoría
     */
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * Buscar categoría por ID (puede retornar null para soporte de lógica antigua)
     */
    public Categoria obtenerCategoriaPorId(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria).orElse(null);
    }

    /**
     * Buscar categoría por ID y lanzar excepción si no existe
     */
    public Categoria obtenerCategoriaPorIdOrThrow(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id " + idCategoria));
    }

    /**
     * Actualizar categoría
     */
    public Categoria actualizarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * Eliminar categoría validando existencia
     */
    public void eliminarCategoria(Integer idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new ResourceNotFoundException("Categoría no encontrada con id " + idCategoria);
        }
        categoriaRepository.deleteById(idCategoria);
    }
}