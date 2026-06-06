package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.request.ProductoRequest;
import com.gatashoes.inventario.api.dto.response.ProductoResponse;
import com.gatashoes.inventario.api.mapper.ProductoMapper;
import com.gatashoes.inventario.model.Categoria;
import com.gatashoes.inventario.model.Producto;
import com.gatashoes.inventario.service.CategoriaService;
import com.gatashoes.inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/productos")
@Validated
public class ProductoRestController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarProductos() {
        List<ProductoResponse> responses = productoService.listarProductos().stream()
                .map(ProductoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerProductoPorId(@PathVariable Integer id) {
        Producto producto = productoService.obtenerProductoPorIdOrThrow(id);
        return ResponseEntity.ok(ProductoMapper.toResponse(producto));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crearProducto(@RequestBody @Valid ProductoRequest request) {
        Categoria categoria = categoriaService.obtenerCategoriaPorIdOrThrow(request.idCategoria());

        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setUrlImagen(request.urlImagen());
        producto.setCategoria(categoria);

        Producto productoGuardado = productoService.guardarProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductoMapper.toResponse(productoGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizarProducto(
            @PathVariable Integer id,
            @RequestBody @Valid ProductoRequest request
    ) {
        Producto productoExistente = productoService.obtenerProductoPorIdOrThrow(id);
        Categoria categoria = categoriaService.obtenerCategoriaPorIdOrThrow(request.idCategoria());

        productoExistente.setNombre(request.nombre());
        productoExistente.setDescripcion(request.descripcion());
        productoExistente.setPrecio(request.precio());
        productoExistente.setUrlImagen(request.urlImagen());
        productoExistente.setCategoria(categoria);

        Producto productoActualizado = productoService.actualizarProducto(productoExistente);
        return ResponseEntity.ok(ProductoMapper.toResponse(productoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
