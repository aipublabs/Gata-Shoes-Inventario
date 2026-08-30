package com.gatashoes.inventario.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para recibir datos de registro de un nuevo administrador.
 *
 * Valida que nombre, correo y contraseña sean correctos antes de
 * ser enviados al servicio de negocio.
 *
 * @param nombre Nombre del administrador, obligatorio, máximo 255 caracteres
 * @param correo Email del administrador, obligatorio, formato válido, máximo 255 caracteres
 * @param contrasena Contraseña del administrador, obligatoria, entre 8 y 255 caracteres
 */
public record RegistroRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
        String nombre,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener un formato válido")
        @Size(max = 255, message = "El correo no puede superar los 255 caracteres")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(
                min = 8,
                max = 255,
                message = "La contraseña debe tener entre 8 y 255 caracteres"
        )
        String contrasena
) {
}
