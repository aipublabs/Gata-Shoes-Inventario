package com.gatashoes.inventario.api.exception;

/**
 * Excepción que representa una operación de inventario inválida según la regla
 * de negocio del sistema.
 *
 * <p>Se lanza cuando la operación solicitada no cumple condiciones como:
 * cantidades no válidas, stock resultante negativo o intentos de operación
 * inválida sobre una variante del inventario.</p>
 */
public class OperacionInventarioInvalidaException extends RuntimeException {

    public OperacionInventarioInvalidaException(String message) {
        super(message);
    }
}
