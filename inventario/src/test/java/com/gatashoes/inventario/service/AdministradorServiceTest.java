package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.CorreoDuplicadoException;
import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Administrador;
import com.gatashoes.inventario.repository.AdministradorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdministradorServiceTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdministradorService administradorService;

    @Test
    void listarAdministradores_conRegistros_devuelveLosAdministradoresDelRepositorio() {
        // Dado
        Administrador primero = crearAdministrador(1, "Ana", "ana@prueba.local", "clave-uno");
        Administrador segundo = crearAdministrador(2, "Luis", "luis@prueba.local", "clave-dos");
        List<Administrador> administradores = List.of(primero, segundo);
        when(administradorRepository.findAll()).thenReturn(administradores);

        // Cuando
        List<Administrador> resultado = administradorService.listarAdministradores();

        // Entonces
        assertThat(resultado).containsExactly(primero, segundo);
    }

    @Test
    void listarAdministradores_sinRegistros_devuelveListaVacia() {
        // Dado
        when(administradorRepository.findAll()).thenReturn(List.of());

        // Cuando
        List<Administrador> resultado = administradorService.listarAdministradores();

        // Entonces
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerAdminPorId_conAdministradorExistente_devuelveElAdministrador() {
        // Dado
        Administrador administrador = crearAdministrador(1, "Ana", "ana@prueba.local", "clave-uno");
        when(administradorRepository.findById(1)).thenReturn(Optional.of(administrador));

        // Cuando
        Administrador resultado = administradorService.obtenerAdminPorId(1);

        // Entonces
        assertThat(resultado).isSameAs(administrador);
    }

    @Test
    void obtenerAdminPorId_conAdministradorInexistente_devuelveNull() {
        // Dado
        when(administradorRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        Administrador resultado = administradorService.obtenerAdminPorId(7);

        // Entonces
        assertThat(resultado).isNull();
    }

    @Test
    void obtenerAdminPorIdOrThrow_conAdministradorInexistente_lanzaResourceNotFoundException() {
        // Dado
        when(administradorRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        // Entonces
        assertThatThrownBy(() -> administradorService.obtenerAdminPorIdOrThrow(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Administrador no encontrado con id 7");
    }

    @Test
    void obtenerAdminPorCorreo_conCorreoExistente_devuelveElAdministrador() {
        // Dado
        Administrador administrador = crearAdministrador(1, "Ana", "ana@prueba.local", "clave-uno");
        when(administradorRepository.findByCorreo("ana@prueba.local"))
                .thenReturn(Optional.of(administrador));

        // Cuando
        Optional<Administrador> resultado = administradorService.obtenerAdminPorCorreo("ana@prueba.local");

        // Entonces
        assertThat(resultado).containsSame(administrador);
    }

    @Test
    void obtenerAdminPorCorreo_conCorreoInexistente_devuelveOptionalVacio() {
        // Dado
        when(administradorRepository.findByCorreo("noexiste@prueba.local"))
                .thenReturn(Optional.empty());

        // Cuando
        Optional<Administrador> resultado = administradorService.obtenerAdminPorCorreo("noexiste@prueba.local");

        // Entonces
        assertThat(resultado).isEmpty();
    }

    @Test
    void guardarAdministrador_conEntidadValida_devuelveElAdministradorGuardado() {
        // Dado
        Administrador administrador = crearAdministrador(1, "Ana", "ana@prueba.local", "valor-protegido");
        when(administradorRepository.save(administrador)).thenReturn(administrador);

        // Cuando
        Administrador resultado = administradorService.guardarAdministrador(administrador);

        // Entonces
        assertThat(resultado).isSameAs(administrador);
        verify(administradorRepository).save(administrador);
    }

    @Test
    void actualizarAdministrador_conEntidadExistente_devuelveElAdministradorActualizado() {
        // Dado
        Administrador administrador = crearAdministrador(1, "Ana Actualizada", "ana@prueba.local", "valor-protegido");
        when(administradorRepository.save(administrador)).thenReturn(administrador);

        // Cuando
        Administrador resultado = administradorService.actualizarAdministrador(administrador);

        // Entonces
        assertThat(resultado).isSameAs(administrador);
        verify(administradorRepository).save(administrador);
    }

    @Test
    void registrarAdministrador_conDatosValidos_normalizaProtegeGuardaYDevuelveElAdministrador() {
        // Dado
        Administrador administrador = crearAdministrador(
                null,
                "  Ana Prueba  ",
                "  ANA@PRUEBA.LOCAL  ",
                "clave-de-prueba"
        );
        String contrasenaProtegida = "valor-protegido-simulado";
        when(administradorRepository.existsByCorreoIgnoreCase("ana@prueba.local")).thenReturn(false);
        when(passwordEncoder.encode("clave-de-prueba")).thenReturn(contrasenaProtegida);
        when(administradorRepository.save(administrador)).thenReturn(administrador);

        // Cuando
        Administrador resultado = administradorService.registrarAdministrador(administrador);

        // Entonces
        assertThat(resultado).isSameAs(administrador);
        assertThat(administrador.getNombre()).isEqualTo("Ana Prueba");
        assertThat(administrador.getCorreo()).isEqualTo("ana@prueba.local");
        assertThat(administrador.getContrasena()).isEqualTo(contrasenaProtegida);
        verify(administradorRepository).existsByCorreoIgnoreCase("ana@prueba.local");
        verify(passwordEncoder).encode("clave-de-prueba");
        verify(administradorRepository).save(administrador);
    }

    @Test
    void registrarAdministrador_conCorreoDuplicado_lanzaCorreoDuplicadoExceptionYSinGuardar() {
        // Dado
        Administrador administrador = crearAdministrador(
                null,
                "Ana Prueba",
                "ana@prueba.local",
                "clave-de-prueba"
        );
        when(administradorRepository.existsByCorreoIgnoreCase("ana@prueba.local")).thenReturn(true);

        // Cuando
        // Entonces
        assertThatThrownBy(() -> administradorService.registrarAdministrador(administrador))
                .isInstanceOf(CorreoDuplicadoException.class)
                .hasMessage("El correo ya se encuentra registrado");
        verify(passwordEncoder, never()).encode("clave-de-prueba");
        verify(administradorRepository, never()).save(administrador);
    }

    @Test
    void registrarAdministrador_conContrasenaValida_reemplazaLaContrasenaOriginalPorLaProtegida() {
        // Dado
        String contrasenaOriginal = "clave-de-prueba";
        String contrasenaProtegida = "valor-protegido-simulado";
        Administrador administrador = crearAdministrador(
                null,
                "Ana Prueba",
                "ana@prueba.local",
                contrasenaOriginal
        );
        when(administradorRepository.existsByCorreoIgnoreCase("ana@prueba.local")).thenReturn(false);
        when(passwordEncoder.encode(contrasenaOriginal)).thenReturn(contrasenaProtegida);
        when(administradorRepository.save(administrador)).thenReturn(administrador);

        // Cuando
        Administrador resultado = administradorService.registrarAdministrador(administrador);

        // Entonces
        verify(passwordEncoder).encode(contrasenaOriginal);
        assertThat(resultado.getContrasena()).isEqualTo(contrasenaProtegida);
        assertThat(resultado.getContrasena()).isNotEqualTo(contrasenaOriginal);
        verify(administradorRepository).save(administrador);
    }

    @Test
    void eliminarAdministrador_conIdentificadorExistente_ejecutaDeleteById() {
        // Dado
        when(administradorRepository.existsById(1)).thenReturn(true);

        // Cuando
        administradorService.eliminarAdministrador(1);

        // Entonces
        verify(administradorRepository).deleteById(1);
    }

    @Test
    void eliminarAdministrador_conIdentificadorInexistente_lanzaResourceNotFoundExceptionYSinEliminar() {
        // Dado
        when(administradorRepository.existsById(7)).thenReturn(false);

        // Cuando
        // Entonces
        assertThatThrownBy(() -> administradorService.eliminarAdministrador(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Administrador no encontrado con id 7");
        verify(administradorRepository, never()).deleteById(7);
    }

    private Administrador crearAdministrador(
            Integer id,
            String nombre,
            String correo,
            String contrasena
    ) {
        Administrador administrador = new Administrador();
        administrador.setIdAdmin(id);
        administrador.setNombre(nombre);
        administrador.setCorreo(correo);
        administrador.setContrasena(contrasena);
        return administrador;
    }
}
