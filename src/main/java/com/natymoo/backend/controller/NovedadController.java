package com.natymoo.backend.controller;

import com.natymoo.backend.entity.Novedad;
import com.natymoo.backend.service.NovedadService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD completo para novedades (reportes de incidencias de los usuarios).
 * GET    /api/novedades       -> listar todas
 * GET    /api/novedades/{id}  -> obtener una
 * POST   /api/novedades       -> crear (cualquier usuario autenticado)
 * PUT    /api/novedades/{id}  -> actualizar (admin/empleado cambia estado y respuesta)
 * DELETE /api/novedades/{id}  -> eliminar
 */
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
    public ResponseEntity<Novedad> crear(@Valid @RequestBody Novedad novedad) {
        Novedad creada = service.create(novedad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Novedad> actualizar(@PathVariable Long id, @Valid @RequestBody Novedad novedad) {
        return ResponseEntity.ok(service.update(id, novedad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
