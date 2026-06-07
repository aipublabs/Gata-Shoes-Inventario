package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Producto;
import com.gatashoes.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de gestión de productos.
 * 
 * Esta clase proporciona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para los productos del catálogo de Gata Shoes. Un producto es el articulo base
 * que puede tener múltiples variantes según talla y color.
 * 
 * Responsabilidades:
 * - Gestionar el catálogo de productos
 * - Validar existencia de productos
 * - Facilitar operaciones de persistencia de productos
 */
@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Obtiene la lista completa de todos los productos disponibles en el catálogo.
     * 
     * @return Lista con todos los Producto registrados
     */
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    /**
     * Guarda un nuevo producto en la base de datos o actualiza uno existente.
     * 
     * @param producto Objeto Producto con los datos a guardar
     * @return El Producto guardado con su ID generado (si es nuevo)
     */
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Busca un producto por su identificador único.
     * 
     * @param idProducto ID del producto a buscar
     * @return El Producto encontrado, o null si no existe
     */
    public Producto obtenerProductoPorId(Integer idProducto) {
        return productoRepository.findById(idProducto).orElse(null);
    }

    /**
     * Busca un producto por ID y lanza una excepción si no existe.
     * 
     * @param idProducto ID del producto a buscar
     * @return El Producto encontrado
     * @throws ResourceNotFoundException si el producto no existe
     */
    public Producto obtenerProductoPorIdOrThrow(Integer idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + idProducto));
    }

    /**
     * Actualiza los datos de un producto existente.
     * 
     * @param producto Objeto Producto con los datos actualizados
     * @return El Producto actualizado
     */
    public Producto actualizarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Elimina un producto de la base de datos, validando su existencia previamente.
     * 
     * @param idProducto ID del producto a eliminar
     * @throws ResourceNotFoundException si el producto no existe
     */
    public void eliminarProducto(Integer idProducto) {
        if (!productoRepository.existsById(idProducto)) {
            throw new ResourceNotFoundException("Producto no encontrado con id " + idProducto);
        }
        productoRepository.deleteById(idProducto);
    }
}
