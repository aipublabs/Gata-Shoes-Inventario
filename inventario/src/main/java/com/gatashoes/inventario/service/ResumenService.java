package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.dto.response.CategoriaStockResponse;
import com.gatashoes.inventario.dto.CategoriaStockDTO;
import com.gatashoes.inventario.model.Inventario;
import com.gatashoes.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResumenService {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private InventarioRepository inventarioRepository;

    public ResumenData obtenerResumen() {
        List<Inventario> inventarios = inventarioService.listarInventario();
        List<Inventario> novedades = inventarioService.listarNovedades();
        List<Inventario> topStock = inventarioService.listarTopStock();
        List<CategoriaStockResponse> topCategoriasStock = inventarioRepository
                .findTopCategoriasByStock(PageRequest.of(0, 5)).stream()
                .map(dto -> new CategoriaStockResponse(dto.getNombreCategoria(), dto.getStock()))
                .collect(Collectors.toList());

        long totalVariantes = inventarios.size();
        int totalStock = inventarios.stream()
                .mapToInt(inventario -> inventario.getStock() == null ? 0 : inventario.getStock())
                .sum();
        long alertasStockBajo = inventarios.stream()
                .filter(inventario -> inventario.getStock() != null && inventario.getStock() <= 3)
                .count();

        return new ResumenData(
                totalVariantes,
                totalStock,
                alertasStockBajo,
                topCategoriasStock,
                novedades,
                topStock
        );
    }

    public record ResumenData(
            long totalVariantes,
            int totalStock,
            long alertasStockBajo,
            List<CategoriaStockResponse> topCategoriasStock,
            List<Inventario> novedades,
            List<Inventario> topStock
    ) {
    }
}

