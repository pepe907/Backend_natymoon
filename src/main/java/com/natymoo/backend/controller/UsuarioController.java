package com.natymoo.backend.controller;

import com.natymoo.backend.dto.UsersRequest;
import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.service.UsuarioService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Administración - Usuarios", description = "Requiere rol ADMIN.")
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody UsersRequest usersRequest) {
        Usuario creado = service.create(usersRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @Valid @RequestBody UsersRequest usersRequest) {
        return ResponseEntity.ok(service.update(id, usersRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
