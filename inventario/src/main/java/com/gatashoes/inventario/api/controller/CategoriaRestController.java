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

@RestController
@RequestMapping("/api/v1/categorias")
@Validated
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        List<CategoriaResponse> responses = categoriaService.listarCategorias().stream()
                .map(CategoriaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerCategoriaPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.obtenerCategoriaPorIdOrThrow(id);
        return ResponseEntity.ok(CategoriaMapper.toResponse(categoria));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@RequestBody @Valid CategoriaRequest request) {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombreCategoria(request.nombreCategoria());

        Categoria categoriaGuardada = categoriaService.guardarCategoria(nuevaCategoria);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoriaMapper.toResponse(categoriaGuardada));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
