package com.natymoo.backend.controller;

import com.natymoo.backend.entity.DetalleCarrito;
import com.natymoo.backend.service.DetalleCarritoService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD completo para detalle de carrito.
 * GET    /api/detalle-carrito       -> listar todos
 * GET    /api/detalle-carrito/{id} -> obtener uno
 * POST   /api/detalle-carrito       -> crear
 * PUT    /api/detalle-carrito/{id} -> actualizar
 * DELETE /api/detalle-carrito/{id} -> eliminar
 */
@RestController
@RequestMapping("/api/detalle-carrito")
@RequiredArgsConstructor
@Tag(name = "Carrito - Detalle de carrito", description = "El precioUnitario queda 'congelado' al momento de agregar.")
public class DetalleCarritoController {

    private final DetalleCarritoService service;

    @GetMapping
    public ResponseEntity<List<DetalleCarrito>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleCarrito> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<DetalleCarrito> crear(@Valid @RequestBody DetalleCarrito entidad) {
        DetalleCarrito creado = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleCarrito> actualizar(@PathVariable Long id, @Valid @RequestBody DetalleCarrito entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
