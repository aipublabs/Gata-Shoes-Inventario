package com.gatashoes.inventario.service;

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
     * Buscar categoría por ID
     */
    public Categoria obtenerCategoriaPorId(Integer idCategoria) {

        Optional<Categoria> categoria = categoriaRepository.findById(idCategoria);

        return categoria.orElse(null);
    }

    /**
     * Actualizar categoría
     */
    public Categoria actualizarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * Eliminar categoría
     */
    public void eliminarCategoria(Integer idCategoria) {
        categoriaRepository.deleteById(idCategoria);
    }
}