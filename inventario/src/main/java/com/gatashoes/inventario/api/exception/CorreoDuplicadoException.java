package com.gatashoes.inventario.api.exception;

/**
 * Excepción lanzada cuando se intenta registrar un administrador
 * con un correo que ya existe en la base de datos.
 *
 * Esta excepción permite diferenciar entre un error de validación
 * general y un conflicto específico de correo duplicado, facilitando
 * retornar HTTP 409 Conflict en lugar de 404 Not Found.
 */
public class CorreoDuplicadoException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo.
     *
     * @param message Mensaje de error explicativo en español
     */
    public CorreoDuplicadoException(String message) {
        super(message);
    }
}
