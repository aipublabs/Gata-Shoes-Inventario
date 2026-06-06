package com.gatashoes.inventario.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventarioRequest(
        @NotNull Integer idProducto,
        @NotNull Integer idTalla,
        @NotNull Integer idColor,
        @NotNull @Min(0) Integer stock
) {
}
