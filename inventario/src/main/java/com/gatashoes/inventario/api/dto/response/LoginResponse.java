package com.gatashoes.inventario.api.dto.response;

public record LoginResponse(
        String accessToken,
        Integer idAdmin,
        String nombre,
        String correo
) {
}
