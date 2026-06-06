package com.gatashoes.inventario.api.mapper;

import com.gatashoes.inventario.api.dto.response.CategoriaResponse;
import com.gatashoes.inventario.api.dto.response.ProductoResponse;
import com.gatashoes.inventario.model.Producto;

public class ProductoMapper {

    private ProductoMapper() {
        // utility class
    }

    public static ProductoResponse toResponse(Producto producto) {
        if (producto == null) {
            return null;
        }

        CategoriaResponse categoria = CategoriaMapper.toResponse(producto.getCategoria());

        return new ProductoResponse(
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getUrlImagen(),
                categoria
        );
    }
}
