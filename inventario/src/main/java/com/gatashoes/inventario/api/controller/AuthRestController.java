package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.request.LoginRequest;
import com.gatashoes.inventario.api.dto.response.LoginResponse;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.time.Duration;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
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
}
