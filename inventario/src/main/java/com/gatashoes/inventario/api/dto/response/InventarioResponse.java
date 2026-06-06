package com.gatashoes.inventario.api.dto.response;

public record InventarioResponse(
        Integer idInventario,
        Integer stock,
        ProductoResponse producto,
        TallaResponse talla,
        ColorResponse color
) {
}
