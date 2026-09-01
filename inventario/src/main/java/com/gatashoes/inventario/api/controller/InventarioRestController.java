package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.request.AjusteStockRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

/**
 * Controlador REST para gestión de inventario.
 * 
 * Expone los endpoints de la API REST para realizar operaciones CRUD sobre el inventario.
 * Maneja las peticiones HTTP relacionadas con variantes de productos (combinaciones de
 * producto, talla, color y cantidad de stock disponible).
 * 
 * Base URL: /api/v1/inventario
 */
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

    /**
     * Obtiene la lista de inventario con paginación opcional.
     * 
     * @param page Número de página (opcional, comienza en 0)
     * @param size Cantidad de registros por página (opcional)
     * @return ResponseEntity con lista de InventarioResponse
     * @see InventarioResponse
     */
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

    /**
     * Obtiene un registro de inventario específico por su ID.
     * 
     * @param id Identificador único del inventario
     * @return ResponseEntity con InventarioResponse del registro solicitado
     * @throws ResourceNotFoundException si el inventario no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponse> obtenerInventarioPorId(@PathVariable Integer id) {
        Inventario inventario = inventarioService.obtenerInventarioPorIdOrThrow(id);
        return ResponseEntity.ok(InventarioMapper.toResponse(inventario));
    }

    /**
     * Crea un nuevo registro de inventario.
     * 
     * Valida que el producto, talla y color existan antes de crear el registro.
     * 
     * @param request InventarioRequest con datos del nuevo inventario
     * @return ResponseEntity con InventarioResponse y estado HTTP 201 (CREATED)
     * @throws ResourceNotFoundException si producto, talla o color no existen
     */
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

    /**
     * Actualiza un registro de inventario existente.
     * 
     * @param id Identificador único del inventario a actualizar
     * @param request InventarioRequest con los datos actualizados
     * @return ResponseEntity con InventarioResponse actualizado
     * @throws ResourceNotFoundException si el inventario no existe
     */
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

    /**
     * Ajusta el stock de una variante de inventario según el tipo de operación
     * solicitado: agregar, restar o fijar el stock total.
     *
     * <p>La lógica de cálculo y validación de negocio se ejecuta en el servicio,
     * conservando el controlador como punto de entrada HTTP.</p>
     *
     * @param id Identificador de la variante del inventario a ajustar.
     * @param request Datos del ajuste a aplicar sobre el stock.
     * @return ResponseEntity con el inventario actualizado si el stock final es mayor
     *         que cero; ResponseEntity vacío con estado 204 si el stock resultante es 0
     *         y la variante fue eliminada del inventario.
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<InventarioResponse> ajustarStock(
            @PathVariable Integer id,
            @RequestBody @Valid AjusteStockRequest request
    ) {
        Inventario inventarioActualizado = inventarioService.ajustarStock(id, request.tipo(), request.cantidad());

        if (inventarioActualizado == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(InventarioMapper.toResponse(inventarioActualizado));
    }

    /**
     * Elimina un registro de inventario.
     * 
     * @param id Identificador único del inventario a eliminar
     * @return ResponseEntity vacío con estado HTTP 204 (NO_CONTENT)
     * @throws ResourceNotFoundException si el inventario no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Integer id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }
}
