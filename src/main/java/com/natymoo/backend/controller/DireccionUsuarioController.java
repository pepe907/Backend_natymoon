package com.natymoo.backend.controller;

import com.natymoo.backend.entity.DireccionUsuario;
import com.natymoo.backend.service.DireccionUsuarioService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD completo para direccion de usuario.
 * GET    /api/direcciones-usuario       -> listar todos
 * GET    /api/direcciones-usuario/{id} -> obtener uno
 * POST   /api/direcciones-usuario       -> crear
 * PUT    /api/direcciones-usuario/{id} -> actualizar
 * DELETE /api/direcciones-usuario/{id} -> eliminar
 */
@RestController
@RequestMapping("/api/direcciones-usuario")
@RequiredArgsConstructor
@Tag(name = "Cuenta - Direcciones de usuario", description = "Direcciones de envío del usuario.")
public class DireccionUsuarioController {

    private final DireccionUsuarioService service;

    @GetMapping
    public ResponseEntity<List<DireccionUsuario>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionUsuario> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<DireccionUsuario> crear(@Valid @RequestBody DireccionUsuario entidad) {
        DireccionUsuario creado = service.create(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionUsuario> actualizar(@PathVariable Long id, @Valid @RequestBody DireccionUsuario entidad) {
        return ResponseEntity.ok(service.update(id, entidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
