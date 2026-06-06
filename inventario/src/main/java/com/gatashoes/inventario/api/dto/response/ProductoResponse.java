package com.gatashoes.inventario.api.dto.response;

import java.math.BigDecimal;

public record ProductoResponse(
        Integer idProducto,
        String nombre,
        String descripcion,
        BigDecimal precio,
        String urlImagen,
        CategoriaResponse categoria
) {
}
