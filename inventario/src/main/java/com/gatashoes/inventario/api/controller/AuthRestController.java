package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.request.LoginRequest;
import com.gatashoes.inventario.api.dto.request.RegistroRequest;
import com.gatashoes.inventario.api.dto.response.AdministradorResponse;
import com.gatashoes.inventario.api.dto.response.LoginResponse;
import com.gatashoes.inventario.api.mapper.AdministradorMapper;
import com.gatashoes.inventario.api.security.AuthService;
import com.gatashoes.inventario.api.security.JwtService;
import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.service.AdministradorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.time.Duration;

/**
 * Controlador REST para autenticación y gestión de sesiones.
 * 
 * Expone los endpoints para que los usuarios administradores se autentiquen
 * en la aplicación. Utiliza tokens JWT para mantener sesiones seguras y
 * stateless. Los tokens se almacenan en localStorage en el navegador del cliente.
 * 
 * Base URL: /api/v1/auth
 */
@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthRestController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private JwtService jwtService;

    /**
     * Autentica un usuario administrador y genera tokens JWT.
     * 
     * Valida las credenciales (correo y contraseña) del administrador.
     * Si son correctas, genera:
     * - Access Token: Válido por 15 minutos, se usa en Authorization header
     * - Refresh Token: Válido por 7 días, se almacena en cookie HttpOnly
     * 
     * @param request LoginRequest con correo y contraseña del administrador
     * @return ResponseEntity con LoginResponse (token, ID, nombre, correo) y cookie de refresh
     * @throws AuthenticationException si las credenciales son inválidas
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        Administrador admin = authService.autenticar(request.correo(), request.contrasena());
        String accessToken = jwtService.generateAccessToken(admin);
        String refreshToken = jwtService.generateRefreshToken(admin);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .path("/api/v1/auth")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        LoginResponse response = new LoginResponse(
                accessToken,
                admin.getIdAdmin(),
                admin.getNombre(),
                admin.getCorreo()
        );

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    /**
     * Refresca el access token usando el refresh token.
     * 
     * Cuando el access token expire, el cliente puede usar este endpoint
     * para obtener un nuevo access token sin volver a hacer login.
     * El refresh token se envía automáticamente en cookies.
     * 
     * @param refreshToken Token de refresco enviado en cookie
     * @return ResponseEntity con nuevo access token si es válido
     * @throws UnauthorizedException si el refresh token es inválido o está expirado
     */
    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(
            @CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null || !jwtService.isTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String correo = jwtService.extractCorreo(refreshToken);
        Administrador admin = administradorService.obtenerAdminPorCorreo(correo)
                .orElse(null);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = jwtService.generateAccessToken(admin);
        return ResponseEntity.ok().body(java.util.Map.of("accessToken", accessToken));
    }

    /**
     * Cierra la sesión del usuario.
     * 
     * Invalida el refresh token eliminando la cookie, forzando al cliente
     * a autenticarse nuevamente la próxima vez que su access token expire.
     * 
     * @return ResponseEntity vacío con estado HTTP 200 (OK) y cookie expirada
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .path("/api/v1/auth")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    /**
     * Registra un nuevo administrador en el sistema.
     *
     * Recibe y valida los datos mediante @Valid RegistroRequest.
     * Delega al servicio la normalización del correo, la verificación de duplicados,
     * el cifrado de la contraseña y la persistencia en base de datos.
     *
     * @param request RegistroRequest con nombre, correo y contraseña
     * @return ResponseEntity con AdministradorResponse (sin contraseña) y HTTP 201 Created
     */
    @PostMapping("/registro")
    public ResponseEntity<AdministradorResponse> registro(
            @RequestBody @Valid RegistroRequest request) {

        // Crear entidad Administrador
        Administrador admin = new Administrador();
        admin.setNombre(request.nombre());
        admin.setCorreo(request.correo());
        admin.setContrasena(request.contrasena());

        // Registrar: normaliza, valida duplicados, cifra, persiste
        Administrador registrado = administradorService.registrarAdministrador(admin);

        // Convertir a respuesta sin exponer contraseña
        AdministradorResponse response = AdministradorMapper.toResponse(registrado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
