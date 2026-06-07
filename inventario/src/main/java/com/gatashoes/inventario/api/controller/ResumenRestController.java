package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.response.InventarioResponse;
import com.gatashoes.inventario.api.dto.response.InventarioResumenResponse;
import com.gatashoes.inventario.api.dto.response.CategoriaStockResponse;
import com.gatashoes.inventario.api.mapper.InventarioMapper;
import com.gatashoes.inventario.service.InventarioService;
import com.gatashoes.inventario.service.ResumenService;
import com.gatashoes.inventario.service.ResumenService.ResumenData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para generación de resumen e indicadores del inventario.
 * 
 * Expone los endpoints para obtener estadísticas, métricas y alertas del inventario
 * que se muestran en el panel de control (dashboard) de la aplicación.
 * 
 * Base URL: /api/v1
 */
@RestController
@RequestMapping("/api/v1")
@Validated
public class ResumenRestController {

    @Autowired
    private ResumenService resumenService;

    @Autowired
    private InventarioService inventarioService;

    /**
     * Obtiene el resumen completo del estado del inventario.
     * 
     * @return ResponseEntity con InventarioResumenResponse que contiene:
     *         - Total de variantes
     *         - Stock total disponible
     *         - Cantidad de alertas de stock bajo
     *         - Top 5 categorías con mayor stock
     *         - Últimos 5 productos agregados
     *         - Top 3 productos con mayor stock
     */
    @GetMapping("/resumen")
    public ResponseEntity<InventarioResumenResponse> obtenerResumen() {
        ResumenData resumenData = resumenService.obtenerResumen();

        List<InventarioResponse> novedades = resumenData.novedades().stream()
                .map(InventarioMapper::toResponse)
                .collect(Collectors.toList());

        List<InventarioResponse> topStock = resumenData.topStock().stream()
                .map(InventarioMapper::toResponse)
                .collect(Collectors.toList());

        InventarioResumenResponse response = new InventarioResumenResponse(
                resumenData.totalVariantes(),
                resumenData.totalStock(),
                resumenData.alertasStockBajo(),
                resumenData.topCategoriasStock(),
                novedades,
                topStock
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene la lista de productos con stock crítico o bajo.
     * 
     * Los productos con stock <= 3 se consideran alertas de stock bajo.
     * 
     * @return ResponseEntity con lista de InventarioResponse de alertas
     */
    @GetMapping("/alertas")
    public ResponseEntity<List<InventarioResponse>> listarAlertas() {
        List<InventarioResponse> responses = inventarioService.listarAlertas().stream()
                .map(InventarioMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
