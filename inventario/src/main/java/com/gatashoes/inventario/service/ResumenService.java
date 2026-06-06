package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.dto.response.CategoriaStockResponse;
import com.gatashoes.inventario.model.Inventario;
import com.gatashoes.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumenService {

    @Autowired
    private InventarioRepository inventarioRepository;

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

