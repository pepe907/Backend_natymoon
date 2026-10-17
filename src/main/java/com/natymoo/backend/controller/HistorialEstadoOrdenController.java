package com.natymoo.backend.controller;

import com.natymoo.backend.entity.HistorialEstadoOrden;
import com.natymoo.backend.service.HistorialEstadoOrdenService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD completo para historial de estado de orden.
 * GET    /api/historial-estado-orden       -> listar todos
 * GET    /api/historial-estado-orden/{id} -> obtener uno
 * POST   /api/historial-estado-orden       -> crear
 * PUT    /api/historial-estado-orden/{id} -> actualizar
 * DELETE /api/historial-estado-orden/{id} -> eliminar
 */
@RestController
@RequestMapping("/api/historial-estado-orden")
@RequiredArgsConstructor
@Tag(name = "Compras - Historial de estado de orden", description = "Timeline de seguimiento del pedido.")
public class HistorialEstadoOrdenController {

    private final HistorialEstadoOrdenService service;

    @GetMapping
    public ResponseEntity<List<HistorialEstadoOrden>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialEstadoOrden> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<HistorialEstadoOrden> crear(@Valid @RequestBody HistorialEstadoOrden entidad) {
        HistorialEstadoOrden creado = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistorialEstadoOrden> actualizar(@PathVariable Long id, @Valid @RequestBody HistorialEstadoOrden entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
