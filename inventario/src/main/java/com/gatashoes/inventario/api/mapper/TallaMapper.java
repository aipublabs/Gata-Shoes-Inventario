package com.gatashoes.inventario.api.mapper;

import com.gatashoes.inventario.api.dto.response.TallaResponse;
import com.gatashoes.inventario.model.Talla;

public class TallaMapper {

    private TallaMapper() {
        // utility class
    }

    public static TallaResponse toResponse(Talla talla) {
        if (talla == null) {
            return null;
        }

        return new TallaResponse(
                talla.getIdTalla(),
                talla.getNumero()
        );
    }
}
