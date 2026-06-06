package com.gatashoes.inventario.api.controller;

import com.gatashoes.inventario.api.dto.response.AdministradorResponse;
import com.gatashoes.inventario.api.mapper.AdministradorMapper;
import com.gatashoes.inventario.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/administradores")
@Validated
public class AdministradorRestController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping
    public ResponseEntity<List<AdministradorResponse>> listarAdministradores() {
        List<AdministradorResponse> responses = administradorService.listarAdministradores().stream()
                .map(AdministradorMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponse> obtenerAdministradorPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(AdministradorMapper.toResponse(administradorService.obtenerAdminPorIdOrThrow(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdministrador(@PathVariable Integer id) {
        administradorService.eliminarAdministrador(id);
        return ResponseEntity.noContent().build();
    }
}
