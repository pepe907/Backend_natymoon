package com.natymoo.backend.controller;

import com.natymoo.backend.entity.DetalleOrden;
import com.natymoo.backend.service.DetalleOrdenService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/detalle-orden")
@RequiredArgsConstructor
@Tag(name = "Compras - Detalle de orden", description = "Ítems de una orden de compra.")
public class DetalleOrdenController {

    private final DetalleOrdenService service;

    @GetMapping
    public ResponseEntity<List<DetalleOrden>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleOrden> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<DetalleOrden> crear(@Valid @RequestBody DetalleOrden entidad) {
        DetalleOrden creado = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleOrden> actualizar(@PathVariable Long id, @Valid @RequestBody DetalleOrden entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
