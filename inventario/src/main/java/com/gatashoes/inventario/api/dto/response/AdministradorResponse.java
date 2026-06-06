package com.gatashoes.inventario.api.dto.response;

public record AdministradorResponse(
        Integer idAdmin,
        String nombre,
        String correo
) {
}
