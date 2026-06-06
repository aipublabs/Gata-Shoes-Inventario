package com.gatashoes.inventario.api.dto.response;

import java.util.List;

public record InventarioResumenResponse(
        Long totalVariantes,
        Long totalStock,
        Long alertasStockBajo,
        List<CategoriaStockResponse> topCategoriasStock,
        List<InventarioResponse> novedades,
        List<InventarioResponse> topStock
) {
}
