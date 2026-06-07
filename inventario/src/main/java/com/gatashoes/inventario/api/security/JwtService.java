package com.gatashoes.inventario.api.security;

import com.gatashoes.inventario.model.Administrador;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Servicio de gestión y validación de tokens JWT (JSON Web Tokens).
 * 
 * Esta clase es responsable de:
 * - Generar access tokens y refresh tokens para usuarios autenticados
 * - Validar la integridad y vigencia de los tokens
 * - Extraer información (claims) de los tokens
 * 
 * Los tokens se firman con una clave secreta usando el algoritmo HS256.
 * - Access Token: Tiene una duración de 15 minutos
 * - Refresh Token: Tiene una duración de 7 días
 * 
 * Nota: La clave secreta está hardcodeada en la clase. En producción,
 * debe almacenarse en variables de entorno o gestores de secretos seguros.
 */
@Service
public class JwtService {

    private static final String SECRET_KEY = "D3JkoEtWQzBfbFJjeEgwM0Flb1hYSlpZeFFJVW5Ga1E=";
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000L; // 15 minutos
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L; // 7 días

    /**
     * Genera un access token para un administrador.
     * 
     * El token contiene:
     * - subject (sub): correo del administrador
     * - claim idAdmin: ID del administrador
     * - claim nombre: nombre del administrador
     * - Fecha de emisión (iat)
     * - Fecha de expiración (exp)
     * 
     * @param admin Objeto Administrador para el cual generar el token
     * @return String con el access token firmado en formato JWT
     */
    public String generateAccessToken(Administrador admin) {
        return Jwts.builder()
                .setSubject(admin.getCorreo())
                .claim("idAdmin", admin.getIdAdmin())
                .claim("nombre", admin.getNombre())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Genera un refresh token para un administrador.
     * 
     * El refresh token es más simple que el access token y solo contiene:
     * - subject (sub): correo del administrador
     * - Fecha de emisión (iat)
     * - Fecha de expiración (exp)
     * 
     * Se utiliza para generar nuevos access tokens sin que el usuario vuelva a hacer login.
     * 
     * @param admin Objeto Administrador para el cual generar el token
     * @return String con el refresh token firmado en formato JWT
     */
    public String generateRefreshToken(Administrador admin) {
        return Jwts.builder()
                .setSubject(admin.getCorreo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el correo (subject) del token JWT.
     * 
     * @param token String con el token JWT
     * @return El correo del usuario contenido en el token
     */
    public String extractCorreo(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Valida si un token JWT es válido.
     * 
     * Un token es válido si:
     * - Su firma es correcta
     * - No ha expirado
     * 
     * @param token String con el token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
