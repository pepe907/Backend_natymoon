package com.natymoo.backend.controller;

import com.natymoo.backend.entity.Cupon;
import com.natymoo.backend.service.CuponService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD completo para cupon.
 * GET    /api/cupones       -> listar todos
 * GET    /api/cupones/{id} -> obtener uno
 * POST   /api/cupones       -> crear
 * PUT    /api/cupones/{id} -> actualizar
 * DELETE /api/cupones/{id} -> eliminar
 */
@RestController
@RequestMapping("/api/cupones")
@RequiredArgsConstructor
@Tag(name = "Compras - Cupones", description = "Consultar (GET) requiere estar autenticado; crear/editar/borrar requiere ADMIN.")
public class CuponController {

    private final CuponService service;

    @GetMapping
    public ResponseEntity<List<Cupon>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cupon> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Cupon> crear(@Valid @RequestBody Cupon entidad) {
        Cupon creado = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cupon> actualizar(@PathVariable Integer id, @Valid @RequestBody Cupon entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
