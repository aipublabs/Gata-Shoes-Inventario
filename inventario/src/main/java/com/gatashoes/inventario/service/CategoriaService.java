package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Categoria;
import com.gatashoes.inventario.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de gestión de categorías de productos.
 * 
 * Esta clase gestiona las categorías de clasificación de productos en la tienda.
 * Las categorías organizan los productos por tipo (ej: Zapatos Formales, Deportivos, etc.)
 * y permiten una mejor estructuración del catálogo.
 * 
 * Responsabilidades:
 * - Gestionar categorías de productos
 * - Validar existencia de categorías
 * - Facilitar operaciones de persistencia de categorías
 */
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Obtiene la lista completa de todas las categorías disponibles.
     * 
     * @return Lista con todas las Categoria registradas
     */
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    /**
     * Guarda una nueva categoría en la base de datos.
     * 
     * @param categoria Objeto Categoria con los datos a guardar
     * @return La Categoria guardada con su ID generado
     */
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * Busca una categoría por su identificador único.
     * Este método puede retornar null para mantener compatibilidad con lógica heredada.
     * 
     * @param idCategoria ID de la categoría a buscar
     * @return La Categoria encontrada, o null si no existe
     */
    public Categoria obtenerCategoriaPorId(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria).orElse(null);
    }

    /**
     * Busca una categoría por ID y lanza una excepción si no existe.
     * Use este método cuando requiera garantizar que la categoría exista.
     * 
     * @param idCategoria ID de la categoría a buscar
     * @return La Categoria encontrada
     * @throws ResourceNotFoundException si la categoría no existe
     */
    public Categoria obtenerCategoriaPorIdOrThrow(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id " + idCategoria));
    }

    /**
     * Actualiza los datos de una categoría existente.
     * 
     * @param categoria Objeto Categoria con los datos actualizados
     * @return La Categoria actualizada
     */
    public Categoria actualizarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * Elimina una categoría de la base de datos, validando su existencia previamente.
     * 
     * @param idCategoria ID de la categoría a eliminar
     * @throws ResourceNotFoundException si la categoría no existe
     */
    public void eliminarCategoria(Integer idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new ResourceNotFoundException("Categoría no encontrada con id " + idCategoria);
        }
        categoriaRepository.deleteById(idCategoria);
    }
}