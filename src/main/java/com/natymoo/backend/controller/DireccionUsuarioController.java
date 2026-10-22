package com.natymoo.backend.controller;

import com.natymoo.backend.dto.DireccionUsuarioRequest;
import com.natymoo.backend.entity.DireccionUsuario;
import com.natymoo.backend.service.DireccionUsuarioService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<DireccionUsuario> crear(@Valid @RequestBody DireccionUsuarioRequest direccionUsuarioRequest) {
        DireccionUsuario creado = service.create(direccionUsuarioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionUsuario> actualizar(@PathVariable Long id, @Valid @RequestBody DireccionUsuarioRequest direccionUsuarioRequest) {
        return ResponseEntity.ok(service.update(id,direccionUsuarioRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
