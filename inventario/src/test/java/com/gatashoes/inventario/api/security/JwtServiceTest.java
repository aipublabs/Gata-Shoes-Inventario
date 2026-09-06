package com.gatashoes.inventario.api.security;

import com.gatashoes.inventario.model.Administrador;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateAccessToken_conAdministradorValido_devuelveTokenNoVacio() {
        // Dado
        Administrador administrador = crearAdministrador();

        // Cuando
        String token = jwtService.generateAccessToken(administrador);

        // Entonces
        assertThat(token).isNotBlank();
    }

    @Test
    void extractCorreo_conAccessTokenGenerado_devuelveElCorreoDelAdministrador() {
        // Dado
        Administrador administrador = crearAdministrador();
        String token = jwtService.generateAccessToken(administrador);

        // Cuando
        String correo = jwtService.extractCorreo(token);

        // Entonces
        assertThat(correo).isEqualTo("ana@prueba.local");
    }

    @Test
    void isTokenValid_conAccessTokenGenerado_devuelveVerdadero() {
        // Dado
        String token = jwtService.generateAccessToken(crearAdministrador());

        // Cuando
        boolean resultado = jwtService.isTokenValid(token);

        // Entonces
        assertThat(resultado).isTrue();
    }

    @Test
    void generateRefreshToken_conAdministradorValido_devuelveTokenNoVacio() {
        // Dado
        Administrador administrador = crearAdministrador();

        // Cuando
        String token = jwtService.generateRefreshToken(administrador);

        // Entonces
        assertThat(token).isNotBlank();
    }

    @Test
    void isTokenValid_conRefreshTokenGenerado_devuelveVerdadero() {
        // Dado
        String token = jwtService.generateRefreshToken(crearAdministrador());

        // Cuando
        boolean resultado = jwtService.isTokenValid(token);

        // Entonces
        assertThat(resultado).isTrue();
    }

    @Test
    void isTokenValid_conTokenMalformado_devuelveFalso() {
        // Dado
        String tokenMalformado = "token-malformado";

        // Cuando
        boolean resultado = jwtService.isTokenValid(tokenMalformado);

        // Entonces
        assertThat(resultado).isFalse();
    }

    @Test
    void isTokenValid_conTokenAlterado_devuelveFalso() {
        // Dado
        String token = jwtService.generateAccessToken(crearAdministrador());
        String ultimoCaracter = token.substring(token.length() - 1);
        String reemplazo = ultimoCaracter.equals("a") ? "b" : "a";
        String tokenAlterado = token.substring(0, token.length() - 1) + reemplazo;

        // Cuando
        boolean resultado = jwtService.isTokenValid(tokenAlterado);

        // Entonces
        assertThat(resultado).isFalse();
    }

    private Administrador crearAdministrador() {
        Administrador administrador = new Administrador();
        administrador.setIdAdmin(1);
        administrador.setNombre("Ana Prueba");
        administrador.setCorreo("ana@prueba.local");
        return administrador;
    }
}
