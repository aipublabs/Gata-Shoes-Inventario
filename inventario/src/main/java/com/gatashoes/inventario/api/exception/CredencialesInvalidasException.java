package com.gatashoes.inventario.api.exception;

/**
 * Excepción lanzada cuando un intento de autenticación falla
 * debido a credenciales inválidas (correo inexistente o contraseña incorrecta).
 *
 * Esta excepción permite diferenciar un fallo de autenticación
 * de otros errores, facilitando retornar HTTP 401 Unauthorized.
 */
public class CredencialesInvalidasException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo.
     *
     * @param message Mensaje de error explicativo en español
     */
    public CredencialesInvalidasException(String message) {
        super(message);
    }
}
