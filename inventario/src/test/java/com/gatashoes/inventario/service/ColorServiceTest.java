package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Color;
import com.gatashoes.inventario.repository.ColorRepository;
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
class ColorServiceTest {

    @Mock
    private ColorRepository colorRepository;

    @InjectMocks
    private ColorService colorService;

    @Test
    void listarColores_conRegistros_devuelveLosColoresDelRepositorio() {
        // Dado
        Color primero = crearColor(1, "Negro");
        Color segundo = crearColor(2, "Blanco");
        List<Color> colores = List.of(primero, segundo);
        when(colorRepository.findAll()).thenReturn(colores);

        // Cuando
        List<Color> resultado = colorService.listarColores();

        // Entonces
        assertThat(resultado).containsExactly(primero, segundo);
    }

    @Test
    void listarColores_sinRegistros_devuelveListaVacia() {
        // Dado
        when(colorRepository.findAll()).thenReturn(List.of());

        // Cuando
        List<Color> resultado = colorService.listarColores();

        // Entonces
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerColorPorId_conColorExistente_devuelveElColor() {
        // Dado
        Color color = crearColor(1, "Negro");
        when(colorRepository.findById(1)).thenReturn(Optional.of(color));

        // Cuando
        Color resultado = colorService.obtenerColorPorId(1);

        // Entonces
        assertThat(resultado).isSameAs(color);
    }

    @Test
    void obtenerColorPorId_conColorInexistente_devuelveNull() {
        // Dado
        when(colorRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        Color resultado = colorService.obtenerColorPorId(7);

        // Entonces
        assertThat(resultado).isNull();
    }

    @Test
    void obtenerColorPorIdOrThrow_conColorExistente_devuelveElColor() {
        // Dado
        Color color = crearColor(1, "Negro");
        when(colorRepository.findById(1)).thenReturn(Optional.of(color));

        // Cuando
        Color resultado = colorService.obtenerColorPorIdOrThrow(1);

        // Entonces
        assertThat(resultado).isSameAs(color);
    }

    @Test
    void obtenerColorPorIdOrThrow_conColorInexistente_lanzaResourceNotFoundException() {
        // Dado
        when(colorRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        // Entonces
        assertThatThrownBy(() -> colorService.obtenerColorPorIdOrThrow(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Color no encontrado con id 7");
    }

    @Test
    void guardarColor_conEntidadValida_devuelveElColorGuardado() {
        // Dado
        Color color = crearColor(1, "Negro");
        when(colorRepository.save(color)).thenReturn(color);

        // Cuando
        Color resultado = colorService.guardarColor(color);

        // Entonces
        assertThat(resultado).isSameAs(color);
        verify(colorRepository).save(color);
    }

    @Test
    void actualizarColor_conEntidadExistente_devuelveElColorActualizado() {
        // Dado
        Color color = crearColor(1, "Azul");
        when(colorRepository.save(color)).thenReturn(color);

        // Cuando
        Color resultado = colorService.actualizarColor(color);

        // Entonces
        assertThat(resultado).isSameAs(color);
        verify(colorRepository).save(color);
    }

    @Test
    void eliminarColor_conIdentificadorExistente_ejecutaDeleteById() {
        // Dado
        when(colorRepository.existsById(1)).thenReturn(true);

        // Cuando
        colorService.eliminarColor(1);

        // Entonces
        verify(colorRepository).deleteById(1);
    }

    @Test
    void eliminarColor_conIdentificadorInexistente_lanzaResourceNotFoundExceptionYSinEliminar() {
        // Dado
        when(colorRepository.existsById(7)).thenReturn(false);

        // Cuando
        // Entonces
        assertThatThrownBy(() -> colorService.eliminarColor(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Color no encontrado con id 7");
        verify(colorRepository, never()).deleteById(7);
    }

    private Color crearColor(Integer id, String nombre) {
        Color color = new Color();
        color.setIdColor(id);
        color.setNombreColor(nombre);
        return color;
    }
}
