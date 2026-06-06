package com.gatashoes.inventario.api.mapper;

import com.gatashoes.inventario.api.dto.response.ColorResponse;
import com.gatashoes.inventario.api.dto.response.InventarioResponse;
import com.gatashoes.inventario.api.dto.response.TallaResponse;
import com.gatashoes.inventario.model.Inventario;

public class InventarioMapper {

    private InventarioMapper() {
        // utility class
    }

    public static InventarioResponse toResponse(Inventario inventario) {
        if (inventario == null) {
            return null;
        }

        return new InventarioResponse(
                inventario.getIdInventario(),
                inventario.getStock(),
                ProductoMapper.toResponse(inventario.getProducto()),
                TallaMapper.toResponse(inventario.getTalla()),
                ColorMapper.toResponse(inventario.getColor())
        );
    }
}
