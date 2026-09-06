package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Categoria;
import com.gatashoes.inventario.repository.CategoriaRepository;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void listarCategorias_conRegistros_devuelveLasCategoriasDelRepositorio() {
        // Dado
        Categoria primera = crearCategoria(1, "Deportivos");
        Categoria segunda = crearCategoria(2, "Casuales");
        List<Categoria> categorias = List.of(primera, segunda);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        // Cuando
        List<Categoria> resultado = categoriaService.listarCategorias();

        // Entonces
        assertThat(resultado).containsExactly(primera, segunda);
    }

    @Test
    void listarCategorias_sinRegistros_devuelveListaVacia() {
        // Dado
        when(categoriaRepository.findAll()).thenReturn(List.of());

        // Cuando
        List<Categoria> resultado = categoriaService.listarCategorias();

        // Entonces
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerCategoriaPorId_conCategoriaExistente_devuelveLaCategoria() {
        // Dado
        Categoria categoria = crearCategoria(1, "Deportivos");
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        // Cuando
        Categoria resultado = categoriaService.obtenerCategoriaPorId(1);

        // Entonces
        assertThat(resultado).isSameAs(categoria);
    }

    @Test
    void obtenerCategoriaPorId_conCategoriaInexistente_devuelveNull() {
        // Dado
        when(categoriaRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        Categoria resultado = categoriaService.obtenerCategoriaPorId(7);

        // Entonces
        assertThat(resultado).isNull();
    }

    @Test
    void obtenerCategoriaPorIdOrThrow_conCategoriaExistente_devuelveLaCategoria() {
        // Dado
        Categoria categoria = crearCategoria(1, "Deportivos");
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        // Cuando
        Categoria resultado = categoriaService.obtenerCategoriaPorIdOrThrow(1);

        // Entonces
        assertThat(resultado).isSameAs(categoria);
    }

    @Test
    void obtenerCategoriaPorIdOrThrow_conCategoriaInexistente_lanzaResourceNotFoundException() {
        // Dado
        when(categoriaRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        // Entonces
        assertThatThrownBy(() -> categoriaService.obtenerCategoriaPorIdOrThrow(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Categoría no encontrada con id 7");
    }

    @Test
    void guardarCategoria_conEntidadValida_devuelveLaCategoriaGuardada() {
        // Dado
        Categoria categoria = crearCategoria(1, "Deportivos");
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        // Cuando
        Categoria resultado = categoriaService.guardarCategoria(categoria);

        // Entonces
        assertThat(resultado).isSameAs(categoria);
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void actualizarCategoria_conEntidadExistente_devuelveLaCategoriaActualizada() {
        // Dado
        Categoria categoria = crearCategoria(1, "Casuales");
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        // Cuando
        Categoria resultado = categoriaService.actualizarCategoria(categoria);

        // Entonces
        assertThat(resultado).isSameAs(categoria);
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void eliminarCategoria_conIdentificadorExistente_ejecutaDeleteById() {
        // Dado
        when(categoriaRepository.existsById(1)).thenReturn(true);

        // Cuando
        categoriaService.eliminarCategoria(1);

        // Entonces
        verify(categoriaRepository).deleteById(1);
    }

    @Test
    void eliminarCategoria_conIdentificadorInexistente_lanzaResourceNotFoundExceptionYSinEliminar() {
        // Dado
        when(categoriaRepository.existsById(7)).thenReturn(false);

        // Cuando
        // Entonces
        assertThatThrownBy(() -> categoriaService.eliminarCategoria(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Categoría no encontrada con id 7");
        verify(categoriaRepository, never()).deleteById(7);
    }

    private Categoria crearCategoria(Integer id, String nombre) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(id);
        categoria.setNombreCategoria(nombre);
        return categoria;
    }
}
