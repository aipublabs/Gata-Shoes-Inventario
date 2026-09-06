package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.dto.response.CategoriaStockResponse;
import com.gatashoes.inventario.model.Inventario;
import com.gatashoes.inventario.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
class ResumenServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private ResumenService resumenService;

    @Test
    void obtenerResumen_conInformacionCompleta_construyeTodosLosCampos() {
        // Dado
        Inventario novedad = crearInventario(1, 4);
        Inventario mayorStock = crearInventario(2, 12);
        List<CategoriaStockResponse> categorias = List.of(
                new CategoriaStockResponse("Deportivos", 12L)
        );
        List<Inventario> novedades = List.of(novedad);
        List<Inventario> topStock = List.of(mayorStock);
        when(inventarioRepository.count()).thenReturn(2L);
        when(inventarioRepository.sumTotalStock()).thenReturn(16L);
        when(inventarioRepository.countStockBajo()).thenReturn(1L);
        when(inventarioRepository.findTopCategoriasByStock(eq(PageRequest.of(0, 5))))
            .thenReturn(categorias);
        when(inventarioRepository.findTop5ByOrderByIdInventarioDesc()).thenReturn(novedades);
        when(inventarioRepository.findTop3ByOrderByStockDesc()).thenReturn(topStock);

        // Cuando
        ResumenService.ResumenData resultado = resumenService.obtenerResumen();

        // Entonces
        assertThat(resultado.totalVariantes()).isEqualTo(2L);
        assertThat(resultado.totalStock()).isEqualTo(16L);
        assertThat(resultado.alertasStockBajo()).isEqualTo(1L);
        assertThat(resultado.topCategoriasStock()).containsExactlyElementsOf(categorias);
        assertThat(resultado.novedades()).containsExactlyElementsOf(novedades);
        assertThat(resultado.topStock()).containsExactlyElementsOf(topStock);
        verify(inventarioRepository).findTopCategoriasByStock(PageRequest.of(0, 5));
    }

    @Test
    void obtenerResumen_conStockTotalNulo_utilizaCero() {
        // Dado
        prepararResumen(0L, null, 0L, List.of(), List.of(), List.of());

        // Cuando
        ResumenService.ResumenData resultado = resumenService.obtenerResumen();

        // Entonces
        assertThat(resultado.totalStock()).isZero();
    }

    @Test
    void obtenerResumen_conCantidadDeAlertasNula_utilizaCero() {
        // Dado
        prepararResumen(0L, 0L, null, List.of(), List.of(), List.of());

        // Cuando
        ResumenService.ResumenData resultado = resumenService.obtenerResumen();

        // Entonces
        assertThat(resultado.alertasStockBajo()).isZero();
    }

    @Test
    void obtenerResumen_conInventarioVacio_devuelveCerosYListasVacias() {
        // Dado
        prepararResumen(0L, 0L, 0L, List.of(), List.of(), List.of());

        // Cuando
        ResumenService.ResumenData resultado = resumenService.obtenerResumen();

        // Entonces
        assertThat(resultado).isNotNull();
        assertThat(resultado.totalVariantes()).isZero();
        assertThat(resultado.totalStock()).isZero();
        assertThat(resultado.alertasStockBajo()).isZero();
        assertThat(resultado.topCategoriasStock()).isEmpty();
        assertThat(resultado.novedades()).isEmpty();
        assertThat(resultado.topStock()).isEmpty();
    }

    @Test
    void obtenerResumen_conDistribucionPorCategoria_conservaLaListaYUsaPaginaCeroTamanoCinco() {
        // Dado
        List<CategoriaStockResponse> categorias = List.of(
                new CategoriaStockResponse("Casuales", 9L),
                new CategoriaStockResponse("Deportivos", 6L)
        );
        prepararResumen(2L, 15L, 1L, categorias, List.of(), List.of());

        // Cuando
        ResumenService.ResumenData resultado = resumenService.obtenerResumen();

        // Entonces
        assertThat(resultado.topCategoriasStock()).containsExactlyElementsOf(categorias);
        verify(inventarioRepository).findTopCategoriasByStock(PageRequest.of(0, 5));
    }

    @Test
    void obtenerResumen_conNovedadesDelRepositorio_conservaLaListaDeNovedades() {
        // Dado
        List<Inventario> novedades = List.of(crearInventario(1, 2));
        prepararResumen(1L, 2L, 1L, List.of(), novedades, List.of());

        // Cuando
        ResumenService.ResumenData resultado = resumenService.obtenerResumen();

        // Entonces
        assertThat(resultado.novedades()).containsExactlyElementsOf(novedades);
    }

    @Test
    void obtenerResumen_conProductosDeMayorStock_conservaLaListaDeMayorStock() {
        // Dado
        List<Inventario> topStock = List.of(crearInventario(1, 10));
        prepararResumen(1L, 10L, 0L, List.of(), List.of(), topStock);

        // Cuando
        ResumenService.ResumenData resultado = resumenService.obtenerResumen();

        // Entonces
        assertThat(resultado.topStock()).containsExactlyElementsOf(topStock);
    }

    private void prepararResumen(
            long totalVariantes,
            Long totalStock,
            Long alertasStockBajo,
            List<CategoriaStockResponse> categorias,
            List<Inventario> novedades,
            List<Inventario> topStock
    ) {
        when(inventarioRepository.count()).thenReturn(totalVariantes);
        when(inventarioRepository.sumTotalStock()).thenReturn(totalStock);
        when(inventarioRepository.countStockBajo()).thenReturn(alertasStockBajo);
        when(inventarioRepository.findTopCategoriasByStock(eq(PageRequest.of(0, 5))))
            .thenReturn(categorias);
        when(inventarioRepository.findTop5ByOrderByIdInventarioDesc()).thenReturn(novedades);
        when(inventarioRepository.findTop3ByOrderByStockDesc()).thenReturn(topStock);
    }

    private Inventario crearInventario(Integer id, Integer stock) {
        Inventario inventario = new Inventario();
        inventario.setIdInventario(id);
        inventario.setStock(stock);
        return inventario;
    }
}
