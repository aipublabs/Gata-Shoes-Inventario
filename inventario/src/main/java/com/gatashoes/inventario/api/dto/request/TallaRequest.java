package com.gatashoes.inventario.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TallaRequest(
        @NotBlank
        @Size(max = 10)
        String numero
) {
}
