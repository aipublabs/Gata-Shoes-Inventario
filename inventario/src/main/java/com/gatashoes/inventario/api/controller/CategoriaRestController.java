package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.request.CategoriaRequest;
import com.gatashoes.inventario.api.dto.response.CategoriaResponse;
import com.gatashoes.inventario.api.mapper.CategoriaMapper;
import com.gatashoes.inventario.model.Categoria;
import com.gatashoes.inventario.service.CategoriaService;
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

/**
 * Controlador REST para gestión de categorías de productos.
 * 
 * Expone los endpoints de la API REST para realizar operaciones CRUD sobre las categorías
 * que clasifican y organizan los productos del catálogo.
 * 
 * Base URL: /api/v1/categorias
 */
@RestController
@RequestMapping("/api/v1/categorias")
@Validated
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * Obtiene la lista completa de todas las categorías.
     * 
     * @return ResponseEntity con lista de CategoriaResponse
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        List<CategoriaResponse> responses = categoriaService.listarCategorias().stream()
                .map(CategoriaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Obtiene una categoría específica por su ID.
     * 
     * @param id Identificador único de la categoría
     * @return ResponseEntity con CategoriaResponse de la categoría solicitada
     * @throws ResourceNotFoundException si la categoría no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerCategoriaPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.obtenerCategoriaPorIdOrThrow(id);
        return ResponseEntity.ok(CategoriaMapper.toResponse(categoria));
    }

    /**
     * Crea una nueva categoría.
     * 
     * @param request CategoriaRequest con los datos de la nueva categoría
     * @return ResponseEntity con CategoriaResponse y estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@RequestBody @Valid CategoriaRequest request) {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombreCategoria(request.nombreCategoria());

        Categoria categoriaGuardada = categoriaService.guardarCategoria(nuevaCategoria);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoriaMapper.toResponse(categoriaGuardada));
    }

    /**
     * Actualiza una categoría existente.
     * 
     * @param id Identificador único de la categoría a actualizar
     * @param request CategoriaRequest con los datos actualizados
     * @return ResponseEntity con CategoriaResponse actualizada
     * @throws ResourceNotFoundException si la categoría no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(
            @PathVariable Integer id,
            @RequestBody @Valid CategoriaRequest request
    ) {
        Categoria categoriaExistente = categoriaService.obtenerCategoriaPorIdOrThrow(id);
        categoriaExistente.setNombreCategoria(request.nombreCategoria());

        Categoria categoriaActualizada = categoriaService.actualizarCategoria(categoriaExistente);
        return ResponseEntity.ok(CategoriaMapper.toResponse(categoriaActualizada));
    }

    /**
     * Elimina una categoría.
     * 
     * @param id Identificador único de la categoría a eliminar
     * @return ResponseEntity vacío con estado HTTP 204 (NO_CONTENT)
     * @throws ResourceNotFoundException si la categoría no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
