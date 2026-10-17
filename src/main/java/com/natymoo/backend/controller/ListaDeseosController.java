package com.natymoo.backend.controller;

import com.natymoo.backend.entity.ListaDeseos;
import com.natymoo.backend.service.ListaDeseosService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD completo para lista de deseos.
 * GET    /api/lista-deseos       -> listar todos
 * GET    /api/lista-deseos/{id} -> obtener uno
 * POST   /api/lista-deseos       -> crear
 * PUT    /api/lista-deseos/{id} -> actualizar
 * DELETE /api/lista-deseos/{id} -> eliminar
 */
@RestController
@RequestMapping("/api/lista-deseos")
@RequiredArgsConstructor
@Tag(name = "Carrito - Lista de deseos", description = "Favoritos del usuario.")
public class ListaDeseosController {

    private final ListaDeseosService service;

    @GetMapping
    public ResponseEntity<List<ListaDeseos>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaDeseos> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ListaDeseos> crear(@Valid @RequestBody ListaDeseos entidad) {
        ListaDeseos creado = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaDeseos> actualizar(@PathVariable Long id, @Valid @RequestBody ListaDeseos entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
