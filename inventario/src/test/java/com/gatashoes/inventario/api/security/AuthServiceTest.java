package com.gatashoes.inventario.api.security;

import com.gatashoes.inventario.api.exception.CredencialesInvalidasException;
import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.service.AdministradorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AdministradorService administradorService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @ParameterizedTest
    @ValueSource(strings = {"$2a$", "$2b$", "$2y$"})
    void autenticar_conContrasenaProtegidaReconocida_devuelveElAdministrador(String prefijoProtegido) {
        // Dado
        Administrador administrador = crearAdministrador("ana@prueba.local", prefijoProtegido + "valor-de-prueba");
        when(administradorService.obtenerAdminPorCorreo("ana@prueba.local"))
                .thenReturn(Optional.of(administrador));
        when(passwordEncoder.matches("clave-de-prueba", administrador.getContrasena()))
                .thenReturn(true);

        // Cuando
        Administrador resultado = authService.autenticar("ana@prueba.local", "clave-de-prueba");

        // Entonces
        assertThat(resultado).isSameAs(administrador);
        verify(passwordEncoder).matches("clave-de-prueba", administrador.getContrasena());
        verify(administradorService, never()).actualizarAdministrador(administrador);
    }

    @Test
    void autenticar_conContrasenaProtegidaIncorrecta_lanzaCredencialesInvalidasException() {
        // Dado
        Administrador administrador = crearAdministrador("ana@prueba.local", "$2b$valor-de-prueba");
        when(administradorService.obtenerAdminPorCorreo("ana@prueba.local"))
                .thenReturn(Optional.of(administrador));
        when(passwordEncoder.matches("clave-incorrecta", administrador.getContrasena()))
                .thenReturn(false);

        // Cuando
        // Entonces
        assertThatThrownBy(() -> authService.autenticar("ana@prueba.local", "clave-incorrecta"))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessage("Credenciales inválidas");
        verify(administradorService, never()).actualizarAdministrador(administrador);
    }

    @Test
    void autenticar_conCorreoInexistente_lanzaCredencialesInvalidasExceptionSinConsultarContrasena() {
        // Dado
        when(administradorService.obtenerAdminPorCorreo("noexiste@prueba.local"))
                .thenReturn(Optional.empty());

        // Cuando
        // Entonces
        assertThatThrownBy(() -> authService.autenticar("noexiste@prueba.local", "clave-de-prueba"))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessage("Credenciales inválidas");
        verifyNoInteractions(passwordEncoder);
        verify(administradorService, never()).actualizarAdministrador(null);
    }

    @Test
    void autenticar_conContrasenaAlmacenadaNula_lanzaCredencialesInvalidasException() {
        // Dado
        Administrador administrador = crearAdministrador("ana@prueba.local", null);
        when(administradorService.obtenerAdminPorCorreo("ana@prueba.local"))
                .thenReturn(Optional.of(administrador));

        // Cuando
        // Entonces
        assertThatThrownBy(() -> authService.autenticar("ana@prueba.local", "clave-de-prueba"))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessage("Credenciales inválidas");
        verifyNoInteractions(passwordEncoder);
        verify(administradorService, never()).actualizarAdministrador(administrador);
    }

    @Test
    void autenticar_conContrasenaSinProteccionCorrecta_migraLaContrasenaYDevuelveElAdministrador() {
        // Dado
        Administrador administrador = crearAdministrador("ana@prueba.local", "clave-de-prueba");
        String valorProtegido = "valor-protegido-simulado";
        when(administradorService.obtenerAdminPorCorreo("ana@prueba.local"))
                .thenReturn(Optional.of(administrador));
        when(passwordEncoder.encode("clave-de-prueba")).thenReturn(valorProtegido);
        when(administradorService.actualizarAdministrador(administrador)).thenReturn(administrador);

        // Cuando
        Administrador resultado = authService.autenticar("ana@prueba.local", "clave-de-prueba");

        // Entonces
        assertThat(resultado).isSameAs(administrador);
        assertThat(administrador.getContrasena()).isEqualTo(valorProtegido);
        verify(passwordEncoder).encode("clave-de-prueba");
        verify(administradorService).actualizarAdministrador(administrador);
    }

    @Test
    void autenticar_conContrasenaSinProteccionIncorrecta_lanzaCredencialesInvalidasExceptionSinMigrar() {
        // Dado
        Administrador administrador = crearAdministrador("ana@prueba.local", "clave-almacenada");
        when(administradorService.obtenerAdminPorCorreo("ana@prueba.local"))
                .thenReturn(Optional.of(administrador));

        // Cuando
        // Entonces
        assertThatThrownBy(() -> authService.autenticar("ana@prueba.local", "clave-incorrecta"))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessage("Credenciales inválidas");
        verifyNoInteractions(passwordEncoder);
        verify(administradorService, never()).actualizarAdministrador(administrador);
    }

    private Administrador crearAdministrador(String correo, String contrasena) {
        Administrador administrador = new Administrador();
        administrador.setNombre("Ana Prueba");
        administrador.setCorreo(correo);
        administrador.setContrasena(contrasena);
        return administrador;
    }
}
