package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.dto.request.TipoAjusteStock;
import com.gatashoes.inventario.api.exception.OperacionInventarioInvalidaException;
import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Inventario;
import com.gatashoes.inventario.repository.InventarioRepository;
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
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void listarInventario_conRegistros_devuelveLosRegistrosDelRepositorio() {
        // Dado
        Inventario primero = crearInventario(1, 5);
        Inventario segundo = crearInventario(2, 3);
        List<Inventario> registros = List.of(primero, segundo);
        when(inventarioRepository.findAll()).thenReturn(registros);

        // Cuando
        List<Inventario> resultado = inventarioService.listarInventario();

        // Entonces
        assertThat(resultado).containsExactly(primero, segundo);
    }

    @Test
    void listarInventario_sinRegistros_devuelveListaVacia() {
        // Dado
        when(inventarioRepository.findAll()).thenReturn(List.of());

        // Cuando
        List<Inventario> resultado = inventarioService.listarInventario();

        // Entonces
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerInventarioPorId_conRegistroExistente_devuelveElInventario() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        // Cuando
        Inventario resultado = inventarioService.obtenerInventarioPorId(1);

        // Entonces
        assertThat(resultado).isSameAs(inventario);
    }

    @Test
    void obtenerInventarioPorId_conRegistroInexistente_devuelveNull() {
        // Dado
        when(inventarioRepository.findById(1)).thenReturn(Optional.empty());

        // Cuando
        Inventario resultado = inventarioService.obtenerInventarioPorId(1);

        // Entonces
        assertThat(resultado).isNull();
    }

    @Test
    void obtenerInventarioPorIdOrThrow_conRegistroInexistente_lanzaResourceNotFoundException() {
        // Dado
        when(inventarioRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        // Entonces
        assertThatThrownBy(() -> inventarioService.obtenerInventarioPorIdOrThrow(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Inventario no encontrado con id 7");
    }

    @Test
    void guardarInventario_conEntidadNueva_devuelveElInventarioGuardado() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        // Cuando
        Inventario resultado = inventarioService.guardarInventario(inventario);

        // Entonces
        assertThat(resultado).isSameAs(inventario);
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void actualizarInventario_conEntidadExistente_devuelveElInventarioActualizado() {
        // Dado
        Inventario inventario = crearInventario(1, 8);
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        // Cuando
        Inventario resultado = inventarioService.actualizarInventario(inventario);

        // Entonces
        assertThat(resultado).isSameAs(inventario);
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void eliminarInventario_conIdentificadorExistente_eliminaElInventario() {
        // Dado
        when(inventarioRepository.existsById(1)).thenReturn(true);

        // Cuando
        inventarioService.eliminarInventario(1);

        // Entonces
        verify(inventarioRepository).deleteById(1);
    }

    @Test
    void eliminarInventario_conIdentificadorInexistente_lanzaResourceNotFoundException() {
        // Dado
        when(inventarioRepository.existsById(7)).thenReturn(false);

        // Cuando
        // Entonces
        assertThatThrownBy(() -> inventarioService.eliminarInventario(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Inventario no encontrado con id 7");
        verify(inventarioRepository, never()).deleteById(7);
    }

    @Test
    void listarAlertas_conStockBajoCeroYCuatroYNull_incluyeSoloStockHastaTresNoNulo() {
        // Dado
        Inventario stockTres = crearInventario(1, 3);
        Inventario stockCero = crearInventario(2, 0);
        Inventario stockCuatro = crearInventario(3, 4);
        Inventario stockNulo = crearInventario(4, null);
        when(inventarioRepository.findAll())
                .thenReturn(List.of(stockTres, stockCero, stockCuatro, stockNulo));

        // Cuando
        List<Inventario> resultado = inventarioService.listarAlertas();

        // Entonces
        assertThat(resultado).containsExactly(stockTres, stockCero);
    }

    @Test
    void listarNovedades_conRegistrosRecientes_devuelveLaListaDelRepositorio() {
        // Dado
        Inventario primero = crearInventario(2, 5);
        Inventario segundo = crearInventario(1, 3);
        List<Inventario> novedades = List.of(primero, segundo);
        when(inventarioRepository.findTop5ByOrderByIdInventarioDesc()).thenReturn(novedades);

        // Cuando
        List<Inventario> resultado = inventarioService.listarNovedades();

        // Entonces
        assertThat(resultado).containsExactly(primero, segundo);
    }

    @Test
    void listarTopStock_conRegistrosOrdenados_devuelveLaListaDelRepositorio() {
        // Dado
        Inventario primero = crearInventario(1, 10);
        Inventario segundo = crearInventario(2, 8);
        List<Inventario> topStock = List.of(primero, segundo);
        when(inventarioRepository.findTop3ByOrderByStockDesc()).thenReturn(topStock);

        // Cuando
        List<Inventario> resultado = inventarioService.listarTopStock();

        // Entonces
        assertThat(resultado).containsExactly(primero, segundo);
    }

    @Test
    void ajustarStock_conAgregarDosUnidades_actualizaElStockASiete() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        // Cuando
        Inventario resultado = inventarioService.ajustarStock(1, TipoAjusteStock.AGREGAR, 2);

        // Entonces
        assertThat(resultado).isSameAs(inventario);
        assertThat(inventario.getStock()).isEqualTo(7);
        verify(inventarioRepository).save(inventario);
        verify(inventarioRepository, never()).deleteById(1);
    }

    @Test
    void ajustarStock_conRestarDosUnidades_actualizaElStockATres() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        // Cuando
        Inventario resultado = inventarioService.ajustarStock(1, TipoAjusteStock.RESTAR, 2);

        // Entonces
        assertThat(resultado).isSameAs(inventario);
        assertThat(inventario.getStock()).isEqualTo(3);
        verify(inventarioRepository).save(inventario);
        verify(inventarioRepository, never()).deleteById(1);
    }

    @Test
    void ajustarStock_conFijarEnOcho_actualizaElStockAOcho() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        // Cuando
        Inventario resultado = inventarioService.ajustarStock(1, TipoAjusteStock.FIJAR, 8);

        // Entonces
        assertThat(resultado).isSameAs(inventario);
        assertThat(inventario.getStock()).isEqualTo(8);
        verify(inventarioRepository).save(inventario);
        verify(inventarioRepository, never()).deleteById(1);
    }

    @Test
    void ajustarStock_conAgregarCero_lanzaOperacionInventarioInvalidaException() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        // Cuando
        // Entonces
        assertThatThrownBy(() -> inventarioService.ajustarStock(1, TipoAjusteStock.AGREGAR, 0))
                .isInstanceOf(OperacionInventarioInvalidaException.class)
                .hasMessage("La cantidad debe ser mayor que cero para agregar o restar stock");
        verify(inventarioRepository, never()).save(inventario);
        verify(inventarioRepository, never()).deleteById(1);
    }

    @Test
    void ajustarStock_conRestarCero_lanzaOperacionInventarioInvalidaException() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        // Cuando
        // Entonces
        assertThatThrownBy(() -> inventarioService.ajustarStock(1, TipoAjusteStock.RESTAR, 0))
                .isInstanceOf(OperacionInventarioInvalidaException.class)
                .hasMessage("La cantidad debe ser mayor que cero para agregar o restar stock");
        verify(inventarioRepository, never()).save(inventario);
        verify(inventarioRepository, never()).deleteById(1);
    }

    @Test
    void ajustarStock_conRestarPorEncimaDelStock_lanzaOperacionInventarioInvalidaException() {
        // Dado
        Inventario inventario = crearInventario(1, 3);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        // Cuando
        // Entonces
        assertThatThrownBy(() -> inventarioService.ajustarStock(1, TipoAjusteStock.RESTAR, 4))
                .isInstanceOf(OperacionInventarioInvalidaException.class)
                .hasMessage("El stock resultante no puede ser negativo");
        assertThat(inventario.getStock()).isEqualTo(3);
        verify(inventarioRepository, never()).save(inventario);
        verify(inventarioRepository, never()).deleteById(1);
    }

    @Test
    void ajustarStock_conRestarHastaCero_eliminaElInventarioYDevuelveNull() {
        // Dado
        Inventario inventario = crearInventario(1, 3);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        // Cuando
        Inventario resultado = inventarioService.ajustarStock(1, TipoAjusteStock.RESTAR, 3);

        // Entonces
        assertThat(resultado).isNull();
        verify(inventarioRepository).deleteById(1);
        verify(inventarioRepository, never()).save(inventario);
    }

    @Test
    void ajustarStock_conFijarEnCero_eliminaElInventarioYDevuelveNull() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        // Cuando
        Inventario resultado = inventarioService.ajustarStock(1, TipoAjusteStock.FIJAR, 0);

        // Entonces
        assertThat(resultado).isNull();
        verify(inventarioRepository).deleteById(1);
        verify(inventarioRepository, never()).save(inventario);
    }

    @Test
    void ajustarStock_conIdentificadorInexistente_lanzaResourceNotFoundException() {
        // Dado
        Inventario inventario = crearInventario(9, 5);
        when(inventarioRepository.findById(9)).thenReturn(Optional.empty());

        // Cuando
        // Entonces
        assertThatThrownBy(() -> inventarioService.ajustarStock(9, TipoAjusteStock.AGREGAR, 2))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("9");
        verify(inventarioRepository, never()).save(inventario);
        verify(inventarioRepository, never()).deleteById(9);
    }

    @Test
    void ajustarStock_conAgregarCantidadNula_lanzaOperacionInventarioInvalidaException() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        // Cuando
        // Entonces
        assertThatThrownBy(() -> inventarioService.ajustarStock(1, TipoAjusteStock.AGREGAR, null))
                .isInstanceOf(OperacionInventarioInvalidaException.class);
        verify(inventarioRepository, never()).save(inventario);
        verify(inventarioRepository, never()).deleteById(1);
    }

    @Test
    void ajustarStock_conRestarCantidadNula_lanzaOperacionInventarioInvalidaException() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        // Cuando
        // Entonces
        assertThatThrownBy(() -> inventarioService.ajustarStock(1, TipoAjusteStock.RESTAR, null))
                .isInstanceOf(OperacionInventarioInvalidaException.class);
        verify(inventarioRepository, never()).save(inventario);
        verify(inventarioRepository, never()).deleteById(1);
    }

    @Test
    void ajustarStock_conFijarCantidadNula_lanzaOperacionInventarioInvalidaException() {
        // Dado
        Inventario inventario = crearInventario(1, 5);
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));

        // Cuando y Entonces
        assertThatThrownBy(() -> inventarioService.ajustarStock(1, TipoAjusteStock.FIJAR, null))
                .isInstanceOf(OperacionInventarioInvalidaException.class)
                .hasMessage("La cantidad es obligatoria");
        verify(inventarioRepository, never()).save(inventario);
        verify(inventarioRepository, never()).deleteById(1);
    }

    private Inventario crearInventario(Integer id, Integer stock) {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(id);
        inventario.setStock(stock);
        return inventario;
    }
}
