package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.dto.response.CategoriaStockResponse;
import com.gatashoes.inventario.model.Inventario;
import com.gatashoes.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de generación de resumen e indicadores del inventario.
 * 
 * Esta clase es responsable de compilar datos estadísticos y métricas del inventario
 * para presentar en el panel de control (dashboard) de la aplicación.
 * Proporciona información sobre el estado general de la tienda incluyendo alertas,
 * productos destacados y análisis de categorías.
 * 
 * Responsabilidades:
 * - Calcular métricas de inventario
 * - Generar datos para el dashboard
 * - Compilar alertas y estadísticas
 */
@Service
public class ResumenService {

    @Autowired
    private InventarioRepository inventarioRepository;

    /**
     * Obtiene un resumen completo del estado del inventario con todas las métricas.
     * 
     * Este método compila datos de:
     * - Total de variantes de productos
     * - Cantidad total de stock disponible
     * - Cantidad de alertas de stock bajo
     * - Categorías con mayor volumen de stock
     * - Productos agregados recientemente
     * - Productos con mayor cantidad de stock
     * 
     * @return ResumenData con todos los datos estadísticos del inventario
     */
    public ResumenData obtenerResumen() {
        long totalVariantes = inventarioRepository.count();
        Long totalStock = inventarioRepository.sumTotalStock();
        Long alertasStockBajo = inventarioRepository.countStockBajo();
        List<CategoriaStockResponse> topCategoriasStock = inventarioRepository
                .findTopCategoriasByStock(PageRequest.of(0, 5));
        List<Inventario> novedades = inventarioRepository.findTop5ByOrderByIdInventarioDesc();
        List<Inventario> topStock = inventarioRepository.findTop3ByOrderByStockDesc();

        return new ResumenData(
                totalVariantes,
                totalStock != null ? totalStock : 0L,
                alertasStockBajo != null ? alertasStockBajo : 0L,
                topCategoriasStock,
                novedades,
                topStock
        );
    }

    public record ResumenData(
            long totalVariantes,
            long totalStock,
            long alertasStockBajo,
            List<CategoriaStockResponse> topCategoriasStock,
            List<Inventario> novedades,
            List<Inventario> topStock
    ) {
    }
}

