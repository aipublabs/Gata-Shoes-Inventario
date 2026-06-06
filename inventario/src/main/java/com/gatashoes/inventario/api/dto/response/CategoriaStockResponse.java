package com.gatashoes.inventario.api.dto.response;

public record CategoriaStockResponse(
        String nombreCategoria,
        Long stock
) {
}
