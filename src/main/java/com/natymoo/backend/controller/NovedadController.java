package com.natymoo.backend.controller;

import com.natymoo.backend.dto.NovedadRequest;
import com.natymoo.backend.entity.Novedad;
import com.natymoo.backend.service.NovedadService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/novedades")
@RequiredArgsConstructor
@Tag(name = "Compras - Novedades (reportes)", description = "Sistema de reportes/incidencias de los clientes.")
public class NovedadController {

    private final NovedadService service;

    @GetMapping
    public ResponseEntity<List<Novedad>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Novedad> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Novedad> crear(@Valid @RequestBody NovedadRequest novedadRequest) {
        Novedad creada = service.create(novedadRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Novedad> actualizar(@PathVariable Long id, @Valid @RequestBody NovedadRequest novedadRequest) {
        return ResponseEntity.ok(service.update(id, novedadRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
