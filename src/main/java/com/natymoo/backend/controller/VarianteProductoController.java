package com.natymoo.backend.controller;

import com.natymoo.backend.entity.VarianteProducto;
import com.natymoo.backend.service.VarianteProductoService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD completo para variante de producto.
 * GET    /api/variantes-producto       -> listar todos
 * GET    /api/variantes-producto/{id} -> obtener uno
 * POST   /api/variantes-producto       -> crear
 * PUT    /api/variantes-producto/{id} -> actualizar
 * DELETE /api/variantes-producto/{id} -> eliminar
 */
@RestController
@RequestMapping("/api/variantes-producto")
@RequiredArgsConstructor
@Tag(name = "Catálogo - Variantes de producto", description = "Cada combinación talla+color es una variante con su propio stock y SKU. Lectura pública.")
public class VarianteProductoController {

    private final VarianteProductoService service;

    @GetMapping
    public ResponseEntity<List<VarianteProducto>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VarianteProducto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<VarianteProducto> crear(@Valid @RequestBody VarianteProducto entidad) {
        VarianteProducto creado = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VarianteProducto> actualizar(@PathVariable Long id, @Valid @RequestBody VarianteProducto entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
