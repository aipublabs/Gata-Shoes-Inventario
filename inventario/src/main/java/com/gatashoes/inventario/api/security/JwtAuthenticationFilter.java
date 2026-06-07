package com.gatashoes.inventario.api.security;

import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.service.AdministradorService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro de autenticación basado en JWT para solicitudes HTTP.
 * 
 * Este filtro intercepta todas las solicitudes HTTP (excepto las públicas) y:
 * 1. Extrae el token JWT del header "Authorization: Bearer <token>"
 * 2. Valida la integridad y vigencia del token
 * 3. Carga los datos del usuario en el contexto de seguridad de Spring
 * 
 * Si el token es inválido o no existe, la solicitud continua sin autenticación
 * y será rechazada en los endpoints protegidos por Spring Security.
 * 
 * Este filtro se ejecuta una vez por solicitud (OncePerRequestFilter).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AdministradorService administradorService;

    /**
     * Filtra la solicitud HTTP verificando el token JWT.
     * 
     * Algoritmo:
     * 1. Obtiene el header "Authorization" de la solicitud
     * 2. Si existe y comienza con "Bearer ", extrae el token
     * 3. Valida el token usando JwtService
     * 4. Si es válido, extrae el correo y carga el usuario desde la base de datos
     * 5. Crea un objeto de autenticación y lo almacena en el contexto de Spring Security
     * 6. Continua con la cadena de filtros (doFilter)
     * 
     * @param request Solicitud HTTP
     * @param response Respuesta HTTP
     * @param filterChain Cadena de filtros de Spring Security
     * @throws ServletException excepción de servlet
     * @throws IOException excepción de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtService.isTokenValid(token)) {
                String correo = jwtService.extractCorreo(token);
                administradorService.obtenerAdminPorCorreo(correo).ifPresent(admin -> {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            admin,
                            null,
                            List.of()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        }

        filterChain.doFilter(request, response);
    }
}
