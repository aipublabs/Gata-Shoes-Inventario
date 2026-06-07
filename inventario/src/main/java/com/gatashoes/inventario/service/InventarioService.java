package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Inventario;
import com.gatashoes.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de gestión de inventario.
 * 
 * Esta clase maneja todas las operaciones relacionadas con el inventario de la tienda,
 * incluyendo la consulta, creación, actualización y eliminación de variantes de productos
 * (combinaciones de producto, talla y color con cantidad en stock).
 * 
 * Responsabilidades:
 * - Gestionar consultas de inventario
 * - Validar existencia de registros
 * - Generar alertas de stock bajo
 * - Obtener estadísticas de productos recientes y más vendidos
 */
@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    /**
     * Obtiene la lista completa de todas las variantes del inventario.
     * 
     * @return Lista con todos los registros de inventario disponibles
     */
    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }

    /**
     * Guarda un nuevo registro de inventario o actualiza uno existente.
     * 
     * @param inventario Objeto Inventario con los datos a guardar
     * @return El objeto Inventario guardado con su ID generado (si es nuevo)
     */
    public Inventario guardarInventario(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    /**
     * Busca un registro de inventario por su identificador único.
     * 
     * @param idInventario ID del inventario a buscar
     * @return El Inventario encontrado, o null si no existe
     */
    public Inventario obtenerInventarioPorId(Integer idInventario) {
        return inventarioRepository.findById(idInventario).orElse(null);
    }

    /**
     * Busca un registro de inventario por ID y lanza una excepción si no existe.
     * 
     * @param idInventario ID del inventario a buscar
     * @return El Inventario encontrado
     * @throws ResourceNotFoundException si el inventario no existe
     */
    public Inventario obtenerInventarioPorIdOrThrow(Integer idInventario) {
        return inventarioRepository.findById(idInventario)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id " + idInventario));
    }

    /**
     * Actualiza los datos de un registro de inventario existente.
     * 
     * @param inventario Objeto Inventario con los datos actualizados
     * @return El Inventario actualizado
     */
    public Inventario actualizarInventario(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    /**
     * Elimina un registro de inventario, validando que exista previamente.
     * 
     * @param idInventario ID del inventario a eliminar
     * @throws ResourceNotFoundException si el inventario no existe
     */
    public void eliminarInventario(Integer idInventario) {
        if (!inventarioRepository.existsById(idInventario)) {
            throw new ResourceNotFoundException("Inventario no encontrado con id " + idInventario);
        }
        inventarioRepository.deleteById(idInventario);
    }

    /**
     * Obtiene todos los registros de inventario con stock bajo o crítico (<=3 unidades).
     * 
     * @return Lista de Inventario con stock crítico para generar alertas
     */
    public List<Inventario> listarAlertas() {
        return inventarioRepository.findAll().stream()
                .filter(inventario -> inventario.getStock() != null && inventario.getStock() <= 3)
                .toList();
    }

    /**
     * Obtiene los 5 productos más recientemente agregados al inventario.
     * 
     * @return Lista de los 5 Inventario más recientes, ordenados descendentemente por ID
     */
    public List<Inventario> listarNovedades() {
        return inventarioRepository.findTop5ByOrderByIdInventarioDesc();
    }

    /**
     * Obtiene los 3 productos con mayor cantidad de stock disponible.
     * 
     * @return Lista de los 3 Inventario con más stock, ordenados descendentemente
     */
    public List<Inventario> listarTopStock() {
        return inventarioRepository.findTop3ByOrderByStockDesc();
    }
}
