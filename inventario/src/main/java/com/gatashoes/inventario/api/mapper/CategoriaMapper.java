package com.gatashoes.inventario.api.mapper;

import com.gatashoes.inventario.api.dto.response.CategoriaResponse;
import com.gatashoes.inventario.model.Categoria;

public class CategoriaMapper {

    private CategoriaMapper() {
        // utility class
    }

    public static CategoriaResponse toResponse(Categoria categoria) {
        if (categoria == null) {
            return null;
        }

        return new CategoriaResponse(
                categoria.getIdCategoria(),
                categoria.getNombreCategoria()
        );
    }
}
