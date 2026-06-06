package com.gatashoes.inventario.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ColorRequest(
        @NotBlank
        @Size(max = 30)
        String nombreColor
) {
}
