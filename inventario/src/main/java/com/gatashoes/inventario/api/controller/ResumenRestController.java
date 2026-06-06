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

@RestController
@RequestMapping("/api/v1")
@Validated
public class ResumenRestController {

    @Autowired
    private ResumenService resumenService;

    @Autowired
    private InventarioService inventarioService;

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

    @GetMapping("/alertas")
    public ResponseEntity<List<InventarioResponse>> listarAlertas() {
        List<InventarioResponse> responses = inventarioService.listarAlertas().stream()
                .map(InventarioMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
