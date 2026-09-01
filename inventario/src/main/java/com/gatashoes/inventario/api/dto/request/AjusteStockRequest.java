package com.gatashoes.inventario.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO de entrada para ajustar el stock de una variante del inventario.
 *
 * <p>Permite definir si se desea agregar, restar o fijar el stock total
 * disponible para una variante concreta.</p>
 *
 * @param tipo Tipo de ajuste a aplicar sobre el stock.
 * @param cantidad Cantidad a sumar, restar o fijar según el tipo de operación.
 */
public record AjusteStockRequest(
        @NotNull(message = "El tipo de ajuste es obligatorio")
        TipoAjusteStock tipo,

        @NotNull(message = "La cantidad es obligatoria")
        @PositiveOrZero(message = "La cantidad no puede ser negativa")
        Integer cantidad
) {
}
