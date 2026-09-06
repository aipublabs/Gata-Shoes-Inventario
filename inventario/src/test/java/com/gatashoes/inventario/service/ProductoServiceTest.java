package com.gatashoes.inventario.service;

import com.gatashoes.inventario.api.exception.ResourceNotFoundException;
import com.gatashoes.inventario.model.Categoria;
import com.gatashoes.inventario.model.Producto;
import com.gatashoes.inventario.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void listarProductos_conRegistros_devuelveLosProductosDelRepositorio() {
        // Dado
        Producto primero = crearProducto(1, "Calzado deportivo");
        Producto segundo = crearProducto(2, "Calzado casual");
        List<Producto> productos = List.of(primero, segundo);
        when(productoRepository.findAll()).thenReturn(productos);

        // Cuando
        List<Producto> resultado = productoService.listarProductos();

        // Entonces
        assertThat(resultado).containsExactly(primero, segundo);
    }

    @Test
    void listarProductos_sinRegistros_devuelveListaVacia() {
        // Dado
        when(productoRepository.findAll()).thenReturn(List.of());

        // Cuando
        List<Producto> resultado = productoService.listarProductos();

        // Entonces
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerProductoPorId_conProductoExistente_devuelveElProducto() {
        // Dado
        Producto producto = crearProducto(1, "Calzado deportivo");
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        // Cuando
        Producto resultado = productoService.obtenerProductoPorId(1);

        // Entonces
        assertThat(resultado).isSameAs(producto);
    }

    @Test
    void obtenerProductoPorId_conProductoInexistente_devuelveNull() {
        // Dado
        when(productoRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        Producto resultado = productoService.obtenerProductoPorId(7);

        // Entonces
        assertThat(resultado).isNull();
    }

    @Test
    void obtenerProductoPorIdOrThrow_conProductoExistente_devuelveElProducto() {
        // Dado
        Producto producto = crearProducto(1, "Calzado deportivo");
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        // Cuando
        Producto resultado = productoService.obtenerProductoPorIdOrThrow(1);

        // Entonces
        assertThat(resultado).isSameAs(producto);
    }

    @Test
    void obtenerProductoPorIdOrThrow_conProductoInexistente_lanzaResourceNotFoundException() {
        // Dado
        when(productoRepository.findById(7)).thenReturn(Optional.empty());

        // Cuando
        // Entonces
        assertThatThrownBy(() -> productoService.obtenerProductoPorIdOrThrow(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Producto no encontrado con id 7");
    }

    @Test
    void guardarProducto_conCategoriaEnMemoria_devuelveElProductoGuardado() {
        // Dado
        Producto producto = crearProducto(1, "Calzado deportivo");
        when(productoRepository.save(producto)).thenReturn(producto);

        // Cuando
        Producto resultado = productoService.guardarProducto(producto);

        // Entonces
        assertThat(resultado).isSameAs(producto);
        verify(productoRepository).save(producto);
    }

    @Test
    void actualizarProducto_conEntidadExistente_devuelveElProductoActualizado() {
        // Dado
        Producto producto = crearProducto(1, "Calzado actualizado");
        when(productoRepository.save(producto)).thenReturn(producto);

        // Cuando
        Producto resultado = productoService.actualizarProducto(producto);

        // Entonces
        assertThat(resultado).isSameAs(producto);
        verify(productoRepository).save(producto);
    }

    @Test
    void eliminarProducto_conIdentificadorExistente_ejecutaDeleteById() {
        // Dado
        when(productoRepository.existsById(1)).thenReturn(true);

        // Cuando
        productoService.eliminarProducto(1);

        // Entonces
        verify(productoRepository).deleteById(1);
    }

    @Test
    void eliminarProducto_conIdentificadorInexistente_lanzaResourceNotFoundExceptionYSinEliminar() {
        // Dado
        when(productoRepository.existsById(7)).thenReturn(false);

        // Cuando
        // Entonces
        assertThatThrownBy(() -> productoService.eliminarProducto(7))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Producto no encontrado con id 7");
        verify(productoRepository, never()).deleteById(7);
    }

    private Producto crearProducto(Integer id, String nombre) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(1);
        categoria.setNombreCategoria("Categoría de prueba");

        Producto producto = new Producto();
        producto.setIdProducto(id);
        producto.setNombre(nombre);
        producto.setDescripcion("Descripción de prueba");
        producto.setPrecio(new BigDecimal("100000.00"));
        producto.setUrlImagen(null);
        producto.setCategoria(categoria);
        return producto;
    }
}
