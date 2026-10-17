package com.natymoo.backend.controller;

import com.natymoo.backend.entity.ResenaProducto;
import com.natymoo.backend.service.ResenaProductoService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD completo para resena de producto.
 * GET    /api/resenas-producto       -> listar todos
 * GET    /api/resenas-producto/{id} -> obtener uno
 * POST   /api/resenas-producto       -> crear
 * PUT    /api/resenas-producto/{id} -> actualizar
 * DELETE /api/resenas-producto/{id} -> eliminar
 */
@RestController
@RequestMapping("/api/resenas-producto")
@RequiredArgsConstructor
@Tag(name = "Catálogo - Reseñas de producto", description = "Lectura pública. Calificación de 1 a 5.")
public class ResenaProductoController {

    private final ResenaProductoService service;

    @GetMapping
    public ResponseEntity<List<ResenaProducto>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResenaProducto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ResenaProducto> crear(@Valid @RequestBody ResenaProducto entidad) {
        ResenaProducto creado = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResenaProducto> actualizar(@PathVariable Long id, @Valid @RequestBody ResenaProducto entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
