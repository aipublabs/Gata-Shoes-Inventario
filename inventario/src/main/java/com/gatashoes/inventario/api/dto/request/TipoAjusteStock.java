package com.gatashoes.inventario.api.dto.request;

/**
 * Enumeración que representa los tipos de ajuste permitidos para el stock
 * de una variante de inventario en Gata Shoes.
 *
 * <p>Se utiliza para centralizar la lógica de negocio del stock en el servicio,
 * evitando que el controlador realice cálculos sobre cantidades o validaciones
 * de dominio.</p>
 *
 * <ul>
 *   <li>AGREGAR: suma unidades al stock actual.</li>
 *   <li>RESTAR: descuenta unidades del stock actual.</li>
 *   <li>FIJAR: reemplaza el stock actual por un valor concreto.</li>
 * </ul>
 */
public enum TipoAjusteStock {
    AGREGAR,
    RESTAR,
    FIJAR
}
