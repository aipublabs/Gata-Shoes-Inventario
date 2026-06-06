package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.request.ColorRequest;
import com.gatashoes.inventario.api.dto.response.ColorResponse;
import com.gatashoes.inventario.api.mapper.ColorMapper;
import com.gatashoes.inventario.model.Color;
import com.gatashoes.inventario.service.ColorService;
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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/colores")
@Validated
public class ColorRestController {

    @Autowired
    private ColorService colorService;

    @GetMapping
    public ResponseEntity<List<ColorResponse>> listarColores() {
        List<ColorResponse> responses = colorService.listarColores().stream()
                .map(ColorMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColorResponse> obtenerColorPorId(@PathVariable Integer id) {
        Color color = colorService.obtenerColorPorIdOrThrow(id);
        return ResponseEntity.ok(ColorMapper.toResponse(color));
    }

    @PostMapping
    public ResponseEntity<ColorResponse> crearColor(@RequestBody @Valid ColorRequest request) {
        Color color = new Color();
        color.setNombreColor(request.nombreColor());

        Color colorGuardado = colorService.guardarColor(color);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ColorMapper.toResponse(colorGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColorResponse> actualizarColor(
            @PathVariable Integer id,
            @RequestBody @Valid ColorRequest request
    ) {
        Color colorExistente = colorService.obtenerColorPorIdOrThrow(id);
        colorExistente.setNombreColor(request.nombreColor());

        Color colorActualizado = colorService.actualizarColor(colorExistente);
        return ResponseEntity.ok(ColorMapper.toResponse(colorActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarColor(@PathVariable Integer id) {
        colorService.eliminarColor(id);
        return ResponseEntity.noContent().build();
    }
}
