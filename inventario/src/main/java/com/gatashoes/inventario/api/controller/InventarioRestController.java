package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.request.InventarioRequest;
import com.gatashoes.inventario.api.dto.response.InventarioResponse;
import com.gatashoes.inventario.api.mapper.InventarioMapper;
import com.gatashoes.inventario.model.Color;
import com.gatashoes.inventario.model.Inventario;
import com.gatashoes.inventario.model.Producto;
import com.gatashoes.inventario.model.Talla;
import com.gatashoes.inventario.service.ColorService;
import com.gatashoes.inventario.service.InventarioService;
import com.gatashoes.inventario.service.ProductoService;
import com.gatashoes.inventario.service.TallaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/inventario")
@Validated
public class InventarioRestController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private TallaService tallaService;

    @Autowired
    private ColorService colorService;

    @GetMapping
    public ResponseEntity<List<InventarioResponse>> listarInventario(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        List<Inventario> inventarios = inventarioService.listarInventario();

        if (page == null || size == null) {
            List<InventarioResponse> responses = inventarios.stream()
                    .map(InventarioMapper::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        }

        int fromIndex = Math.max(0, page * size);
        if (fromIndex >= inventarios.size()) {
            return ResponseEntity.ok(List.of());
        }
        int toIndex = Math.min(inventarios.size(), fromIndex + size);
        List<InventarioResponse> responses = inventarios.subList(fromIndex, toIndex).stream()
                .map(InventarioMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponse> obtenerInventarioPorId(@PathVariable Integer id) {
        Inventario inventario = inventarioService.obtenerInventarioPorIdOrThrow(id);
        return ResponseEntity.ok(InventarioMapper.toResponse(inventario));
    }

    @PostMapping
    public ResponseEntity<InventarioResponse> crearInventario(@RequestBody @Valid InventarioRequest request) {
        Producto producto = productoService.obtenerProductoPorIdOrThrow(request.idProducto());
        Talla talla = tallaService.obtenerTallaPorIdOrThrow(request.idTalla());
        Color color = colorService.obtenerColorPorIdOrThrow(request.idColor());

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setTalla(talla);
        inventario.setColor(color);
        inventario.setStock(request.stock());

        Inventario inventarioGuardado = inventarioService.guardarInventario(inventario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(InventarioMapper.toResponse(inventarioGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponse> actualizarInventario(
            @PathVariable Integer id,
            @RequestBody @Valid InventarioRequest request
    ) {
        Inventario inventarioExistente = inventarioService.obtenerInventarioPorIdOrThrow(id);
        Producto producto = productoService.obtenerProductoPorIdOrThrow(request.idProducto());
        Talla talla = tallaService.obtenerTallaPorIdOrThrow(request.idTalla());
        Color color = colorService.obtenerColorPorIdOrThrow(request.idColor());

        inventarioExistente.setProducto(producto);
        inventarioExistente.setTalla(talla);
        inventarioExistente.setColor(color);
        inventarioExistente.setStock(request.stock());

        Inventario inventarioActualizado = inventarioService.actualizarInventario(inventarioExistente);
        return ResponseEntity.ok(InventarioMapper.toResponse(inventarioActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Integer id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }
}
