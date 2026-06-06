package com.gatashoes.inventario.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank
        @Size(max = 100)
        String nombre,
        String descripcion,
        @NotNull
        @Positive
        BigDecimal precio,
        String urlImagen,
        @NotNull
        Integer idCategoria
) {
}
