package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Talla;
import com.gatashoes.inventario.repository.TallaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TallaServiceTest {

    @Mock
    private TallaRepository tallaRepository;

    @InjectMocks
    private TallaService tallaService;

    @Test
    void listarTallas_conRegistros_devuelveLasTallasDelRepositorio() {
        // Dado
        Talla primera = crearTalla(1, "38");
        Talla segunda = crearTalla(2, "39");
        List<Talla> tallas = List.of(primera, segunda);
        when(tallaRepository.findAll()).thenReturn(tallas);

        // Cuando
        List<Talla> resultado = tallaService.listarTallas();

        // Entonces
        assertThat(resultado).containsExactly(primera, segunda);
    }

    @Test
    void listarTallas_sinRegistros_devuelveListaVacia() {
        // Dado
        when(tallaRepository.findAll()).thenReturn(List.of());

        // Cuando
        List<Talla> resultado = tallaService.listarTallas();

        // Entonces
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerTallaPorId_conTallaExistente_devuelveLaTalla() {
        // Dado
        Talla talla = crearTalla(1, "38");
        when(tallaRepository.findById(1)).thenReturn(Optional.of(talla));

        // Cuando
        Talla resultado = tallaService.obtenerTallaPorId(1);

        // Entonces
        assertThat(resultado).isSameAs(talla);
    }

    @Test
    void obtenerTallaPorId_conTallaInexistente_devuelveNull() {
        // Dado
        when(tallaRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        Talla resultado = tallaService.obtenerTallaPorId(7);

        // Entonces
        assertThat(resultado).isNull();
    }

    @Test
    void obtenerTallaPorIdOrThrow_conTallaExistente_devuelveLaTalla() {
        // Dado
        Talla talla = crearTalla(1, "38");
        when(tallaRepository.findById(1)).thenReturn(Optional.of(talla));

        // Cuando
        Talla resultado = tallaService.obtenerTallaPorIdOrThrow(1);

        // Entonces
        assertThat(resultado).isSameAs(talla);
    }

    @Test
    void obtenerTallaPorIdOrThrow_conTallaInexistente_lanzaResourceNotFoundException() {
        // Dado
        when(tallaRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        // Entonces
        assertThatThrownBy(() -> tallaService.obtenerTallaPorIdOrThrow(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Talla no encontrada con id 7");
    }

    @Test
    void guardarTalla_conEntidadValida_devuelveLaTallaGuardada() {
        // Dado
        Talla talla = crearTalla(1, "38");
        when(tallaRepository.save(talla)).thenReturn(talla);

        // Cuando
        Talla resultado = tallaService.guardarTalla(talla);

        // Entonces
        assertThat(resultado).isSameAs(talla);
        verify(tallaRepository).save(talla);
    }

    @Test
    void actualizarTalla_conEntidadExistente_devuelveLaTallaActualizada() {
        // Dado
        Talla talla = crearTalla(1, "40");
        when(tallaRepository.save(talla)).thenReturn(talla);

        // Cuando
        Talla resultado = tallaService.actualizarTalla(talla);

        // Entonces
        assertThat(resultado).isSameAs(talla);
        verify(tallaRepository).save(talla);
    }

    @Test
    void eliminarTalla_conIdentificadorExistente_ejecutaDeleteById() {
        // Dado
        when(tallaRepository.existsById(1)).thenReturn(true);

        // Cuando
        tallaService.eliminarTalla(1);

        // Entonces
        verify(tallaRepository).deleteById(1);
    }

    @Test
    void eliminarTalla_conIdentificadorInexistente_lanzaResourceNotFoundExceptionYSinEliminar() {
        // Dado
        when(tallaRepository.existsById(7)).thenReturn(false);

        // Cuando
        // Entonces
        assertThatThrownBy(() -> tallaService.eliminarTalla(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Talla no encontrada con id 7");
        verify(tallaRepository, never()).deleteById(7);
    }

    private Talla crearTalla(Integer id, String numero) {
        Talla talla = new Talla();
        talla.setIdTalla(id);
        talla.setNumero(numero);
        return talla;
    }
}
