package com.natymoo.backend.controller;

import com.natymoo.backend.dto.RolRequest;
import com.natymoo.backend.entity.Rol;
import com.natymoo.backend.service.RolService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Administración - Roles", description = "Requiere rol ADMIN.")
public class RolController {

    private final RolService service;

    @GetMapping
    public ResponseEntity<List<Rol>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Rol> crear(@Valid @RequestBody RolRequest rolRequest) {
        Rol creado = service.create(rolRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizar(@PathVariable Long id, @Valid @RequestBody RolRequest rolRequest) {
        return ResponseEntity.ok(service.update(id, rolRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
