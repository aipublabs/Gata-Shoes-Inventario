package com.gatashoes.inventario.api.mapper;

import com.gatashoes.inventario.api.dto.response.AdministradorResponse;
import com.gatashoes.inventario.model.Administrador;

public class AdministradorMapper {

    private AdministradorMapper() {
        // utility class
    }

    public static AdministradorResponse toResponse(Administrador admin) {
        if (admin == null) {
            return null;
        }

        return new AdministradorResponse(
                admin.getIdAdmin(),
                admin.getNombre(),
                admin.getCorreo()
        );
    }
}
