package com.natymoo.backend.controller;

import com.natymoo.backend.entity.ImagenProducto;
import com.natymoo.backend.service.ImagenProductoService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD completo para imagen de producto.
 * GET    /api/imagenes-producto       -> listar todos
 * GET    /api/imagenes-producto/{id} -> obtener uno
 * POST   /api/imagenes-producto       -> crear
 * PUT    /api/imagenes-producto/{id} -> actualizar
 * DELETE /api/imagenes-producto/{id} -> eliminar
 */
@RestController
@RequestMapping("/api/imagenes-producto")
@RequiredArgsConstructor
@Tag(name = "Catálogo - Imágenes de producto", description = "Lectura pública.")
public class ImagenProductoController {

    private final ImagenProductoService service;

    @GetMapping
    public ResponseEntity<List<ImagenProducto>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImagenProducto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ImagenProducto> crear(@Valid @RequestBody ImagenProducto entidad) {
        ImagenProducto creado = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImagenProducto> actualizar(@PathVariable Long id, @Valid @RequestBody ImagenProducto entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
