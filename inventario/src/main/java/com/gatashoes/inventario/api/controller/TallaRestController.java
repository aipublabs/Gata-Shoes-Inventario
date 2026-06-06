package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.request.TallaRequest;
import com.gatashoes.inventario.api.dto.response.TallaResponse;
import com.gatashoes.inventario.api.mapper.TallaMapper;
import com.gatashoes.inventario.model.Talla;
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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/tallas")
@Validated
public class TallaRestController {

    @Autowired
    private TallaService tallaService;

    @GetMapping
    public ResponseEntity<List<TallaResponse>> listarTallas() {
        List<TallaResponse> responses = tallaService.listarTallas().stream()
                .map(TallaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TallaResponse> obtenerTallaPorId(@PathVariable Integer id) {
        Talla talla = tallaService.obtenerTallaPorIdOrThrow(id);
        return ResponseEntity.ok(TallaMapper.toResponse(talla));
    }

    @PostMapping
    public ResponseEntity<TallaResponse> crearTalla(@RequestBody @Valid TallaRequest request) {
        Talla talla = new Talla();
        talla.setNumero(request.numero());

        Talla tallaGuardada = tallaService.guardarTalla(talla);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TallaMapper.toResponse(tallaGuardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TallaResponse> actualizarTalla(
            @PathVariable Integer id,
            @RequestBody @Valid TallaRequest request
    ) {
        Talla tallaExistente = tallaService.obtenerTallaPorIdOrThrow(id);
        tallaExistente.setNumero(request.numero());

        Talla tallaActualizada = tallaService.actualizarTalla(tallaExistente);
        return ResponseEntity.ok(TallaMapper.toResponse(tallaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTalla(@PathVariable Integer id) {
        tallaService.eliminarTalla(id);
        return ResponseEntity.noContent().build();
    }
}
