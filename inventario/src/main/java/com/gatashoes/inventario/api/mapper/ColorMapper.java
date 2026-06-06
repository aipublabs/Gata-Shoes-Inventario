package com.gatashoes.inventario.api.mapper;

import com.gatashoes.inventario.api.dto.response.ColorResponse;
import com.gatashoes.inventario.model.Color;

public class ColorMapper {

    private ColorMapper() {
        // utility class
    }

    public static ColorResponse toResponse(Color color) {
        if (color == null) {
            return null;
        }

        return new ColorResponse(
                color.getIdColor(),
                color.getNombreColor()
        );
    }
}
